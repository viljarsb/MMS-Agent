// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: MMTPMessages.proto

package Agent.MMTP.MessageFormats;

public interface RegisterOrBuilder extends
    // @@protoc_insertion_point(interface_extends:Register)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * List of strings representing interests
   * </pre>
   *
   * <code>repeated string interests = 1;</code>
   * @return A list containing the interests.
   */
  java.util.List<String>
      getInterestsList();
  /**
   * <pre>
   * List of strings representing interests
   * </pre>
   *
   * <code>repeated string interests = 1;</code>
   * @return The count of interests.
   */
  int getInterestsCount();
  /**
   * <pre>
   * List of strings representing interests
   * </pre>
   *
   * <code>repeated string interests = 1;</code>
   * @param index The index of the element to return.
   * @return The interests at the given index.
   */
  String getInterests(int index);
  /**
   * <pre>
   * List of strings representing interests
   * </pre>
   *
   * <code>repeated string interests = 1;</code>
   * @param index The index of the value to return.
   * @return The bytes of the interests at the given index.
   */
  com.google.protobuf.ByteString
      getInterestsBytes(int index);

  /**
   * <pre>
   * Boolean indicating if the agent wishes to receive direct messages
   * </pre>
   *
   * <code>optional bool want_direct_messages = 2;</code>
   * @return Whether the wantDirectMessages field is set.
   */
  boolean hasWantDirectMessages();
  /**
   * <pre>
   * Boolean indicating if the agent wishes to receive direct messages
   * </pre>
   *
   * <code>optional bool want_direct_messages = 2;</code>
   * @return The wantDirectMessages.
   */
  boolean getWantDirectMessages();
}
