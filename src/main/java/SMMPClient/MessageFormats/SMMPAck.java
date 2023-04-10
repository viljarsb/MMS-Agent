// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: SMMPMessages.proto

package SMMPClient.MessageFormats;

/**
 * Protobuf type {@code SMMPClient.SMMP.SMMPAck}
 */
public final class SMMPAck extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:SMMPClient.SMMP.SMMPAck)
    SMMPAckOrBuilder {
private static final long serialVersionUID = 0L;
  // Use SMMPAck.newBuilder() to construct.
  private SMMPAck(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private SMMPAck() {
    messageID_ = "";
    signature_ = com.google.protobuf.ByteString.EMPTY;
    certificate_ = com.google.protobuf.ByteString.EMPTY;
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new SMMPAck();
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return SMMPMessages.internal_static_SMMPClient_SMMP_SMMPAck_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return SMMPMessages.internal_static_SMMPClient_SMMP_SMMPAck_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            SMMPAck.class, SMMPAck.Builder.class);
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

  public static final int SIGNATURE_FIELD_NUMBER = 2;
  private com.google.protobuf.ByteString signature_ = com.google.protobuf.ByteString.EMPTY;
  /**
   * <code>bytes signature = 2;</code>
   * @return The signature.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString getSignature() {
    return signature_;
  }

  public static final int CERTIFICATE_FIELD_NUMBER = 3;
  private com.google.protobuf.ByteString certificate_ = com.google.protobuf.ByteString.EMPTY;
  /**
   * <code>bytes certificate = 3;</code>
   * @return The certificate.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString getCertificate() {
    return certificate_;
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
    if (!signature_.isEmpty()) {
      output.writeBytes(2, signature_);
    }
    if (!certificate_.isEmpty()) {
      output.writeBytes(3, certificate_);
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
    if (!signature_.isEmpty()) {
      size += com.google.protobuf.CodedOutputStream
        .computeBytesSize(2, signature_);
    }
    if (!certificate_.isEmpty()) {
      size += com.google.protobuf.CodedOutputStream
        .computeBytesSize(3, certificate_);
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
    if (!(obj instanceof SMMPAck)) {
      return super.equals(obj);
    }
    SMMPAck other = (SMMPAck) obj;

    if (!getMessageID()
        .equals(other.getMessageID())) return false;
    if (!getSignature()
        .equals(other.getSignature())) return false;
    if (!getCertificate()
        .equals(other.getCertificate())) return false;
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
    hash = (37 * hash) + SIGNATURE_FIELD_NUMBER;
    hash = (53 * hash) + getSignature().hashCode();
    hash = (37 * hash) + CERTIFICATE_FIELD_NUMBER;
    hash = (53 * hash) + getCertificate().hashCode();
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static SMMPAck parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static SMMPAck parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static SMMPAck parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static SMMPAck parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static SMMPAck parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static SMMPAck parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static SMMPAck parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static SMMPAck parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static SMMPAck parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static SMMPAck parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static SMMPAck parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static SMMPAck parseFrom(
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
  public static Builder newBuilder(SMMPAck prototype) {
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
   * Protobuf type {@code SMMPClient.SMMP.SMMPAck}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:SMMPClient.SMMP.SMMPAck)
          SMMPAckOrBuilder
  {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return SMMPMessages.internal_static_SMMPClient_SMMP_SMMPAck_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return SMMPMessages.internal_static_SMMPClient_SMMP_SMMPAck_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              SMMPAck.class, SMMPAck.Builder.class);
    }

    // Construct using SMMPClient.SMMP.SMMPAck.newBuilder()
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
      signature_ = com.google.protobuf.ByteString.EMPTY;
      certificate_ = com.google.protobuf.ByteString.EMPTY;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return SMMPMessages.internal_static_SMMPClient_SMMP_SMMPAck_descriptor;
    }

    @java.lang.Override
    public SMMPAck getDefaultInstanceForType() {
      return SMMPAck.getDefaultInstance();
    }

    @java.lang.Override
    public SMMPAck build() {
      SMMPAck result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public SMMPAck buildPartial() {
      SMMPAck result = new SMMPAck(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(SMMPAck result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.messageID_ = messageID_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.signature_ = signature_;
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.certificate_ = certificate_;
      }
    }

    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof SMMPAck) {
        return mergeFrom((SMMPAck)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(SMMPAck other) {
      if (other == SMMPAck.getDefaultInstance()) return this;
      if (!other.getMessageID().isEmpty()) {
        messageID_ = other.messageID_;
        bitField0_ |= 0x00000001;
        onChanged();
      }
      if (other.getSignature() != com.google.protobuf.ByteString.EMPTY) {
        setSignature(other.getSignature());
      }
      if (other.getCertificate() != com.google.protobuf.ByteString.EMPTY) {
        setCertificate(other.getCertificate());
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
            case 18: {
              signature_ = input.readBytes();
              bitField0_ |= 0x00000002;
              break;
            } // case 18
            case 26: {
              certificate_ = input.readBytes();
              bitField0_ |= 0x00000004;
              break;
            } // case 26
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

    private com.google.protobuf.ByteString signature_ = com.google.protobuf.ByteString.EMPTY;
    /**
     * <code>bytes signature = 2;</code>
     * @return The signature.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString getSignature() {
      return signature_;
    }
    /**
     * <code>bytes signature = 2;</code>
     * @param value The signature to set.
     * @return This builder for chaining.
     */
    public Builder setSignature(com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      signature_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>bytes signature = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearSignature() {
      bitField0_ = (bitField0_ & ~0x00000002);
      signature_ = getDefaultInstance().getSignature();
      onChanged();
      return this;
    }

    private com.google.protobuf.ByteString certificate_ = com.google.protobuf.ByteString.EMPTY;
    /**
     * <code>bytes certificate = 3;</code>
     * @return The certificate.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString getCertificate() {
      return certificate_;
    }
    /**
     * <code>bytes certificate = 3;</code>
     * @param value The certificate to set.
     * @return This builder for chaining.
     */
    public Builder setCertificate(com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      certificate_ = value;
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>bytes certificate = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearCertificate() {
      bitField0_ = (bitField0_ & ~0x00000004);
      certificate_ = getDefaultInstance().getCertificate();
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


    // @@protoc_insertion_point(builder_scope:SMMPClient.SMMP.SMMPAck)
  }

  // @@protoc_insertion_point(class_scope:SMMPClient.SMMP.SMMPAck)
  private static final SMMPAck DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new SMMPAck();
  }

  public static SMMPAck getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<SMMPAck>
      PARSER = new com.google.protobuf.AbstractParser<SMMPAck>() {
    @java.lang.Override
    public SMMPAck parsePartialFrom(
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

  public static com.google.protobuf.Parser<SMMPAck> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<SMMPAck> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public SMMPAck getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

