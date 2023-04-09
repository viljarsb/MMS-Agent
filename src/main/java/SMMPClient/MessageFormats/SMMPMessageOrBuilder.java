// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: SMMPMessages.proto

package SMMPClient.MessageFormats;

public interface SMMPMessageOrBuilder extends
    // @@protoc_insertion_point(interface_extends:SMMPClient.SMMP.SMMPMessage)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string messageID = 1;</code>
   * @return The messageID.
   */
  java.lang.String getMessageID();
  /**
   * <code>string messageID = 1;</code>
   * @return The bytes for messageID.
   */
  com.google.protobuf.ByteString
      getMessageIDBytes();

  /**
   * <code>bool isEncrypted = 2;</code>
   * @return The isEncrypted.
   */
  boolean getIsEncrypted();

  /**
   * <code>bool requiresAck = 3;</code>
   * @return The requiresAck.
   */
  boolean getRequiresAck();

  /**
   * <code>bytes signature = 4;</code>
   * @return The signature.
   */
  com.google.protobuf.ByteString getSignature();

  /**
   * <code>bytes certificate = 5;</code>
   * @return The certificate.
   */
  com.google.protobuf.ByteString getCertificate();

  /**
   * <code>bytes payload = 6;</code>
   * @return The payload.
   */
  com.google.protobuf.ByteString getPayload();
}