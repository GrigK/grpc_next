// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: route_guide.proto

package ru.grig.routeguide;

public interface FeatureOrBuilder extends
    // @@protoc_insertion_point(interface_extends:routeguide.Feature)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * The name of the feature.
   * </pre>
   *
   * <code>string name = 1;</code>
   */
  java.lang.String getName();
  /**
   * <pre>
   * The name of the feature.
   * </pre>
   *
   * <code>string name = 1;</code>
   */
  com.google.protobuf.ByteString
      getNameBytes();

  /**
   * <pre>
   * The point where the feature is detected.
   * </pre>
   *
   * <code>.routeguide.Point location = 2;</code>
   */
  boolean hasLocation();
  /**
   * <pre>
   * The point where the feature is detected.
   * </pre>
   *
   * <code>.routeguide.Point location = 2;</code>
   */
  ru.grig.routeguide.Point getLocation();
  /**
   * <pre>
   * The point where the feature is detected.
   * </pre>
   *
   * <code>.routeguide.Point location = 2;</code>
   */
  ru.grig.routeguide.PointOrBuilder getLocationOrBuilder();
}
