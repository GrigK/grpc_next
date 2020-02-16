package ru.grig.grpc_next;

import com.google.protobuf.Message;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import ru.grig.grpc_next.GreeterGrpc;
import ru.grig.grpc_next.HelloReply;
import ru.grig.grpc_next.HelloRequest;
import ru.grig.routeguide.Feature;
import ru.grig.routeguide.Point;
import ru.grig.routeguide.RouteGuideGrpc;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple client that requests a greeting from the {@link HelloWorldServer}.
 */
public class HelloWorldClient {
  private static final Logger logger = Logger.getLogger(HelloWorldClient.class.getName());

  private final ManagedChannel channel;
  private final GreeterGrpc.GreeterBlockingStub blockingStub;
  private RouteGuideGrpc.RouteGuideBlockingStub blockingStubRoute;
  private RouteGuideGrpc.RouteGuideStub asyncStubRoute;

  private Random random = new Random();
//  private TestHelper testHelper;

  /** Construct client connecting to HelloWorld server at {@code host:port}. */
  public HelloWorldClient(String host, int port) {
    this(ManagedChannelBuilder.forAddress(host, port)
        // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
        // needing certificates.
        .usePlaintext()
        .build());
  }

  /** Construct client for accessing HelloWorld server using the existing channel. */
  HelloWorldClient(ManagedChannel channel) {
    this.channel = channel;
    blockingStub = GreeterGrpc.newBlockingStub(this.channel);
    blockingStubRoute = RouteGuideGrpc.newBlockingStub(this.channel);
    asyncStubRoute = RouteGuideGrpc.newStub(this.channel);
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  /** Say hello to server. */
  public void greet(String name) {
    logger.info("Will try to greet " + name + " ...");
    HelloRequest request = HelloRequest.newBuilder().setName(name).build();
    HelloReply response;
    try {
      response = blockingStub.sayHello(request);
    } catch (StatusRuntimeException e) {
      logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
      return;
    }
    logger.info("Greeting: " + response.getMessage());
    try {
      response = blockingStub.sayHelloAgain(request);
    } catch (StatusRuntimeException e) {
      logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
      return;
    }
    logger.info("Greeting Again: " + response.getMessage());
  }

  /**
   * Blocking unary call example.  Calls getFeature and prints the response.
   */
  public void getFeature(int lat, int lon) {
    logger.info(String.format("*** GetFeature: lat=%d lon=%d", lat, lon));

    Point request = Point.newBuilder().setLatitude(lat).setLongitude(lon).build();

    Feature feature;
    try {
      feature = blockingStubRoute.getFeature(request);
    } catch (StatusRuntimeException e) {
      logger.warning(String.format("RPC failed: %s", e.getStatus().toString()));
      return;
    }

    if (RouteGuideUtil.exists(feature)) {
      logger.info(String.format("Found feature called \"%s\" at %f, %f",
              feature.getName(),
              RouteGuideUtil.getLatitude(feature.getLocation()),
              RouteGuideUtil.getLongitude(feature.getLocation())));
    } else {
      logger.info(String.format("Found no feature at %f, %f",
              RouteGuideUtil.getLatitude(feature.getLocation()),
              RouteGuideUtil.getLongitude(feature.getLocation())));
    }
  }

  /**
   * Greet server. If provided, the first element of {@code args} is the name to use in the
   * greeting.
   */
  public static void main(String[] args) throws Exception {
    // Access a service running on the local machine on port 50051
    HelloWorldClient client = new HelloWorldClient("localhost", 50051);
    try {
      String user = "unknown User";
      // Use the arg as the name to greet if provided
      if (args.length > 0) {
        user = args[0];
      }
      client.greet(user + " <<<");
      client.getFeature(0, 0);
      client.getFeature(404839914, -744759616);

    } finally {
      client.shutdown();
    }
  }
}
