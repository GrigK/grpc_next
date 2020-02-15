import io.grpc.testing.GrpcServerRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import ru.grig.GreeterGrpc;
import ru.grig.HelloReply;
import ru.grig.HelloRequest;
import ru.grig.HelloWorldServer;

@RunWith(JUnit4.class)
public class HelloWorldServerTest {
    @Rule
    public final GrpcServerRule grpcServerRule = new GrpcServerRule().directExecutor();

    @Test
    public void greeterImpl_replyMessage(){
        grpcServerRule.getServiceRegistry().addService(new HelloWorldServer.GreeterImpl());

        GreeterGrpc.GreeterBlockingStub blockingStub =
                GreeterGrpc.newBlockingStub(grpcServerRule.getChannel());
        String testName = "test name";

        HelloReply reply = blockingStub.sayHello(HelloRequest.newBuilder().setName(testName).build());

        Assert.assertEquals("Hello " + testName, reply.getMessage());
    }

}
