// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: SMMPMessages.proto

package SMMPClient.MessageFormats;

/**
 * Protobuf type {@code SMMPClient.SMMP.SMMPMessage}
 */
public final class SMMPMessage extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:SMMPClient.SMMP.SMMPMessage)
    SMMPMessageOrBuilder {
private static final long serialVersionUID = 0L;
  // Use SMMPMessage.newBuilder() to construct.
  private SMMPMessage(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private SMMPMessage() {
    messageID_ = "";
    signature_ = com.google.protobuf.ByteString.EMPTY;
    certificate_ = com.google.protobuf.ByteString.EMPTY;
    payload_ = com.google.protobuf.ByteString.EMPTY;
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new SMMPMessage();
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return SMMPClient.MessageFormats.SMMPMessages.internal_static_SMMPClient_SMMP_SMMPMessage_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return SMMPClient.MessageFormats.SMMPMessages.internal_static_SMMPClient_SMMP_SMMPMessage_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            SMMPClient.MessageFormats.SMMPMessage.class, SMMPClient.MessageFormats.SMMPMessage.Builder.class);
  }

  public static final int MESSAGEID_FIELD_NUMBER = 1;
  @SuppressWarnings("serial")
  private volatile java.lang.Object messageID_ = "";
  /**
   * <code>string messageID = 1;</code>
   * @return The messageID.
   */
  @java.lang.Override
  public java.lang.String getMessageID() {
    java.lang.Object ref = messageID_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      messageID_ = s;
      return s;
    }
  }
  /**
   * <code>string messageID = 1;</code>
   * @return The bytes for messageID.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getMessageIDBytes() {
    java.lang.Object ref = messageID_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      messageID_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int ISENCRYPTED_FIELD_NUMBER = 2;
  private boolean isEncrypted_ = false;
  /**
   * <code>bool isEncrypted = 2;</code>
   * @return The isEncrypted.
   */
  @java.lang.Override
  public boolean getIsEncrypted() {
    return isEncrypted_;
  }

  public static final int REQUIRESACK_FIELD_NUMBER = 3;
  private boolean requiresAck_ = false;
  /**
   * <code>bool requiresAck = 3;</code>
   * @return The requiresAck.
   */
  @java.lang.Override
  public boolean getRequiresAck() {
    return requiresAck_;
  }

  public static final int SIGNATURE_FIELD_NUMBER = 4;
  private com.google.protobuf.ByteString signature_ = com.google.protobuf.ByteString.EMPTY;
  /**
   * <code>bytes signature = 4;</code>
   * @return The signature.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString getSignature() {
    return signature_;
  }

  public static final int CERTIFICATE_FIELD_NUMBER = 5;
  private com.google.protobuf.ByteString certificate_ = com.google.protobuf.ByteString.EMPTY;
  /**
   * <code>bytes certificate = 5;</code>
   * @return The certificate.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString getCertificate() {
    return certificate_;
  }

  public static final int PAYLOAD_FIELD_NUMBER = 6;
  private com.google.protobuf.ByteString payload_ = com.google.protobuf.ByteString.EMPTY;
  /**
   * <code>bytes payload = 6;</code>
   * @return The payload.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString getPayload() {
    return payload_;
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(messageID_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, messageID_);
    }
    if (isEncrypted_ != false) {
      output.writeBool(2, isEncrypted_);
    }
    if (requiresAck_ != false) {
      output.writeBool(3, requiresAck_);
    }
    if (!signature_.isEmpty()) {
      output.writeBytes(4, signature_);
    }
    if (!certificate_.isEmpty()) {
      output.writeBytes(5, certificate_);
    }
    if (!payload_.isEmpty()) {
      output.writeBytes(6, payload_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(messageID_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, messageID_);
    }
    if (isEncrypted_ != false) {
      size += com.google.protobuf.CodedOutputStream
        .computeBoolSize(2, isEncrypted_);
    }
    if (requiresAck_ != false) {
      size += com.google.protobuf.CodedOutputStream
        .computeBoolSize(3, requiresAck_);
    }
    if (!signature_.isEmpty()) {
      size += com.google.protobuf.CodedOutputStream
        .computeBytesSize(4, signature_);
    }
    if (!certificate_.isEmpty()) {
      size += com.google.protobuf.CodedOutputStream
        .computeBytesSize(5, certificate_);
    }
    if (!payload_.isEmpty()) {
      size += com.google.protobuf.CodedOutputStream
        .computeBytesSize(6, payload_);
    }
    size += getUnknownFields().getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof SMMPClient.MessageFormats.SMMPMessage)) {
      return super.equals(obj);
    }
    SMMPClient.MessageFormats.SMMPMessage other = (SMMPClient.MessageFormats.SMMPMessage) obj;

    if (!getMessageID()
        .equals(other.getMessageID())) return false;
    if (getIsEncrypted()
        != other.getIsEncrypted()) return false;
    if (getRequiresAck()
        != other.getRequiresAck()) return false;
    if (!getSignature()
        .equals(other.getSignature())) return false;
    if (!getCertificate()
        .equals(other.getCertificate())) return false;
    if (!getPayload()
        .equals(other.getPayload())) return false;
    if (!getUnknownFields().equals(other.getUnknownFields())) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + MESSAGEID_FIELD_NUMBER;
    hash = (53 * hash) + getMessageID().hashCode();
    hash = (37 * hash) + ISENCRYPTED_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
        getIsEncrypted());
    hash = (37 * hash) + REQUIRESACK_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
        getRequiresAck());
    hash = (37 * hash) + SIGNATURE_FIELD_NUMBER;
    hash = (53 * hash) + getSignature().hashCode();
    hash = (37 * hash) + CERTIFICATE_FIELD_NUMBER;
    hash = (53 * hash) + getCertificate().hashCode();
    hash = (37 * hash) + PAYLOAD_FIELD_NUMBER;
    hash = (53 * hash) + getPayload().hashCode();
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static SMMPClient.MessageFormats.SMMPMessage parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static SMMPClient.MessageFormats.SMMPMessage parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static SMMPClient.MessageFormats.SMMPMessage parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static SMMPClient.MessageFormats.SMMPMessage parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static SMMPClient.MessageFormats.SMMPMessage parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static SMMPClient.MessageFormats.SMMPMessage parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static SMMPClient.MessageFormats.SMMPMessage parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static SMMPClient.MessageFormats.SMMPMessage parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static SMMPClient.MessageFormats.SMMPMessage parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static SMMPClient.MessageFormats.SMMPMessage parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static SMMPClient.MessageFormats.SMMPMessage parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static SMMPClient.MessageFormats.SMMPMessage parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(SMMPClient.MessageFormats.SMMPMessage prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code SMMPClient.SMMP.SMMPMessage}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:SMMPClient.SMMP.SMMPMessage)
      SMMPClient.MessageFormats.SMMPMessageOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return SMMPClient.MessageFormats.SMMPMessages.internal_static_SMMPClient_SMMP_SMMPMessage_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return SMMPClient.MessageFormats.SMMPMessages.internal_static_SMMPClient_SMMP_SMMPMessage_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              SMMPClient.MessageFormats.SMMPMessage.class, SMMPClient.MessageFormats.SMMPMessage.Builder.class);
    }

    // Construct using SMMPClient.SMMP.SMMPMessage.newBuilder()
    private Builder() {

    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);

    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      messageID_ = "";
      isEncrypted_ = false;
      requiresAck_ = false;
      signature_ = com.google.protobuf.ByteString.EMPTY;
      certificate_ = com.google.protobuf.ByteString.EMPTY;
      payload_ = com.google.protobuf.ByteString.EMPTY;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return SMMPClient.MessageFormats.SMMPMessages.internal_static_SMMPClient_SMMP_SMMPMessage_descriptor;
    }

    @java.lang.Override
    public SMMPClient.MessageFormats.SMMPMessage getDefaultInstanceForType() {
      return SMMPClient.MessageFormats.SMMPMessage.getDefaultInstance();
    }

    @java.lang.Override
    public SMMPClient.MessageFormats.SMMPMessage build() {
      SMMPClient.MessageFormats.SMMPMessage result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public SMMPClient.MessageFormats.SMMPMessage buildPartial() {
      SMMPClient.MessageFormats.SMMPMessage result = new SMMPClient.MessageFormats.SMMPMessage(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(SMMPClient.MessageFormats.SMMPMessage result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.messageID_ = messageID_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.isEncrypted_ = isEncrypted_;
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.requiresAck_ = requiresAck_;
      }
      if (((from_bitField0_ & 0x00000008) != 0)) {
        result.signature_ = signature_;
      }
      if (((from_bitField0_ & 0x00000010) != 0)) {
        result.certificate_ = certificate_;
      }
      if (((from_bitField0_ & 0x00000020) != 0)) {
        result.payload_ = payload_;
      }
    }

    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof SMMPClient.MessageFormats.SMMPMessage) {
        return mergeFrom((SMMPClient.MessageFormats.SMMPMessage)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(SMMPClient.MessageFormats.SMMPMessage other) {
      if (other == SMMPClient.MessageFormats.SMMPMessage.getDefaultInstance()) return this;
      if (!other.getMessageID().isEmpty()) {
        messageID_ = other.messageID_;
        bitField0_ |= 0x00000001;
        onChanged();
      }
      if (other.getIsEncrypted() != false) {
        setIsEncrypted(other.getIsEncrypted());
      }
      if (other.getRequiresAck() != false) {
        setRequiresAck(other.getRequiresAck());
      }
      if (other.getSignature() != com.google.protobuf.ByteString.EMPTY) {
        setSignature(other.getSignature());
      }
      if (other.getCertificate() != com.google.protobuf.ByteString.EMPTY) {
        setCertificate(other.getCertificate());
      }
      if (other.getPayload() != com.google.protobuf.ByteString.EMPTY) {
        setPayload(other.getPayload());
      }
      this.mergeUnknownFields(other.getUnknownFields());
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 10: {
              messageID_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000001;
              break;
            } // case 10
            case 16: {
              isEncrypted_ = input.readBool();
              bitField0_ |= 0x00000002;
              break;
            } // case 16
            case 24: {
              requiresAck_ = input.readBool();
              bitField0_ |= 0x00000004;
              break;
            } // case 24
            case 34: {
              signature_ = input.readBytes();
              bitField0_ |= 0x00000008;
              break;
            } // case 34
            case 42: {
              certificate_ = input.readBytes();
              bitField0_ |= 0x00000010;
              break;
            } // case 42
            case 50: {
              payload_ = input.readBytes();
              bitField0_ |= 0x00000020;
              break;
            } // case 50
            default: {
              if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                done = true; // was an endgroup tag
              }
              break;
            } // default:
          } // switch (tag)
        } // while (!done)
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.unwrapIOException();
      } finally {
        onChanged();
      } // finally
      return this;
    }
    private int bitField0_;

    private java.lang.Object messageID_ = "";
    /**
     * <code>string messageID = 1;</code>
     * @return The messageID.
     */
    public java.lang.String getMessageID() {
      java.lang.Object ref = messageID_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        messageID_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string messageID = 1;</code>
     * @return The bytes for messageID.
     */
    public com.google.protobuf.ByteString
        getMessageIDBytes() {
      java.lang.Object ref = messageID_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        messageID_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string messageID = 1;</code>
     * @param value The messageID to set.
     * @return This builder for chaining.
     */
    public Builder setMessageID(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      messageID_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>string messageID = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearMessageID() {
      messageID_ = getDefaultInstance().getMessageID();
      bitField0_ = (bitField0_ & ~0x00000001);
      onChanged();
      return this;
    }
    /**
     * <code>string messageID = 1;</code>
     * @param value The bytes for messageID to set.
     * @return This builder for chaining.
     */
    public Builder setMessageIDBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      messageID_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }

    private boolean isEncrypted_ ;
    /**
     * <code>bool isEncrypted = 2;</code>
     * @return The isEncrypted.
     */
    @java.lang.Override
    public boolean getIsEncrypted() {
      return isEncrypted_;
    }
    /**
     * <code>bool isEncrypted = 2;</code>
     * @param value The isEncrypted to set.
     * @return This builder for chaining.
     */
    public Builder setIsEncrypted(boolean value) {

      isEncrypted_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>bool isEncrypted = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearIsEncrypted() {
      bitField0_ = (bitField0_ & ~0x00000002);
      isEncrypted_ = false;
      onChanged();
      return this;
    }

    private boolean requiresAck_ ;
    /**
     * <code>bool requiresAck = 3;</code>
     * @return The requiresAck.
     */
    @java.lang.Override
    public boolean getRequiresAck() {
      return requiresAck_;
    }
    /**
     * <code>bool requiresAck = 3;</code>
     * @param value The requiresAck to set.
     * @return This builder for chaining.
     */
    public Builder setRequiresAck(boolean value) {

      requiresAck_ = value;
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>bool requiresAck = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearRequiresAck() {
      bitField0_ = (bitField0_ & ~0x00000004);
      requiresAck_ = false;
      onChanged();
      return this;
    }

    private com.google.protobuf.ByteString signature_ = com.google.protobuf.ByteString.EMPTY;
    /**
     * <code>bytes signature = 4;</code>
     * @return The signature.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString getSignature() {
      return signature_;
    }
    /**
     * <code>bytes signature = 4;</code>
     * @param value The signature to set.
     * @return This builder for chaining.
     */
    public Builder setSignature(com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      signature_ = value;
      bitField0_ |= 0x00000008;
      onChanged();
      return this;
    }
    /**
     * <code>bytes signature = 4;</code>
     * @return This builder for chaining.
     */
    public Builder clearSignature() {
      bitField0_ = (bitField0_ & ~0x00000008);
      signature_ = getDefaultInstance().getSignature();
      onChanged();
      return this;
    }

    private com.google.protobuf.ByteString certificate_ = com.google.protobuf.ByteString.EMPTY;
    /**
     * <code>bytes certificate = 5;</code>
     * @return The certificate.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString getCertificate() {
      return certificate_;
    }
    /**
     * <code>bytes certificate = 5;</code>
     * @param value The certificate to set.
     * @return This builder for chaining.
     */
    public Builder setCertificate(com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      certificate_ = value;
      bitField0_ |= 0x00000010;
      onChanged();
      return this;
    }
    /**
     * <code>bytes certificate = 5;</code>
     * @return This builder for chaining.
     */
    public Builder clearCertificate() {
      bitField0_ = (bitField0_ & ~0x00000010);
      certificate_ = getDefaultInstance().getCertificate();
      onChanged();
      return this;
    }

    private com.google.protobuf.ByteString payload_ = com.google.protobuf.ByteString.EMPTY;
    /**
     * <code>bytes payload = 6;</code>
     * @return The payload.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString getPayload() {
      return payload_;
    }
    /**
     * <code>bytes payload = 6;</code>
     * @param value The payload to set.
     * @return This builder for chaining.
     */
    public Builder setPayload(com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      payload_ = value;
      bitField0_ |= 0x00000020;
      onChanged();
      return this;
    }
    /**
     * <code>bytes payload = 6;</code>
     * @return This builder for chaining.
     */
    public Builder clearPayload() {
      bitField0_ = (bitField0_ & ~0x00000020);
      payload_ = getDefaultInstance().getPayload();
      onChanged();
      return this;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:SMMPClient.SMMP.SMMPMessage)
  }

  // @@protoc_insertion_point(class_scope:SMMPClient.SMMP.SMMPMessage)
  private static final SMMPClient.MessageFormats.SMMPMessage DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new SMMPClient.MessageFormats.SMMPMessage();
  }

  public static SMMPClient.MessageFormats.SMMPMessage getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<SMMPMessage>
      PARSER = new com.google.protobuf.AbstractParser<SMMPMessage>() {
    @java.lang.Override
    public SMMPMessage parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      Builder builder = newBuilder();
      try {
        builder.mergeFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(builder.buildPartial());
      } catch (com.google.protobuf.UninitializedMessageException e) {
        throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(e)
            .setUnfinishedMessage(builder.buildPartial());
      }
      return builder.buildPartial();
    }
  };

  public static com.google.protobuf.Parser<SMMPMessage> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<SMMPMessage> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public SMMPClient.MessageFormats.SMMPMessage getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

