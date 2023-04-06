// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: MMTPMessages.proto

package Agent.MMTP.MessageFormats;

/**
 * Protobuf type {@code DirectApplicationMessage}
 */
public final class DirectApplicationMessage extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:DirectApplicationMessage)
        DirectApplicationMessageOrBuilder
{
private static final long serialVersionUID = 0L;
  // Use DirectApplicationMessage.newBuilder() to construct.
  private DirectApplicationMessage(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private DirectApplicationMessage() {
    id_ = "";
    recipients_ =
        com.google.protobuf.LazyStringArrayList.emptyList();
    sender_ = "";
    payload_ = com.google.protobuf.ByteString.EMPTY;
  }

  @Override
  @SuppressWarnings({"unused"})
  protected Object newInstance(
      UnusedPrivateParameter unused) {
    return new DirectApplicationMessage();
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return MMTPMessages.internal_static_DirectApplicationMessage_descriptor;
  }

  @Override
  protected FieldAccessorTable
      internalGetFieldAccessorTable() {
    return MMTPMessages.internal_static_DirectApplicationMessage_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            DirectApplicationMessage.class, Builder.class);
  }

  public static final int ID_FIELD_NUMBER = 1;
  @SuppressWarnings("serial")
  private volatile Object id_ = "";
  /**
   * <code>string id = 1;</code>
   * @return The id.
   */
  @Override
  public String getId() {
    Object ref = id_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      id_ = s;
      return s;
    }
  }
  /**
   * <code>string id = 1;</code>
   * @return The bytes for id.
   */
  @Override
  public com.google.protobuf.ByteString
      getIdBytes() {
    Object ref = id_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      id_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int RECIPIENTS_FIELD_NUMBER = 2;
  @SuppressWarnings("serial")
  private com.google.protobuf.LazyStringArrayList recipients_ =
      com.google.protobuf.LazyStringArrayList.emptyList();
  /**
   * <code>repeated string recipients = 2;</code>
   * @return A list containing the recipients.
   */
  public com.google.protobuf.ProtocolStringList
      getRecipientsList() {
    return recipients_;
  }
  /**
   * <code>repeated string recipients = 2;</code>
   * @return The count of recipients.
   */
  public int getRecipientsCount() {
    return recipients_.size();
  }
  /**
   * <code>repeated string recipients = 2;</code>
   * @param index The index of the element to return.
   * @return The recipients at the given index.
   */
  public String getRecipients(int index) {
    return recipients_.get(index);
  }
  /**
   * <code>repeated string recipients = 2;</code>
   * @param index The index of the value to return.
   * @return The bytes of the recipients at the given index.
   */
  public com.google.protobuf.ByteString
      getRecipientsBytes(int index) {
    return recipients_.getByteString(index);
  }

  public static final int SENDER_FIELD_NUMBER = 3;
  @SuppressWarnings("serial")
  private volatile Object sender_ = "";
  /**
   * <code>string sender = 3;</code>
   * @return The sender.
   */
  @Override
  public String getSender() {
    Object ref = sender_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      sender_ = s;
      return s;
    }
  }
  /**
   * <code>string sender = 3;</code>
   * @return The bytes for sender.
   */
  @Override
  public com.google.protobuf.ByteString
      getSenderBytes() {
    Object ref = sender_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      sender_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int EXPIRES_FIELD_NUMBER = 4;
  private com.google.protobuf.Timestamp expires_;
  /**
   * <code>.google.protobuf.Timestamp expires = 4;</code>
   * @return Whether the expires field is set.
   */
  @Override
  public boolean hasExpires() {
    return expires_ != null;
  }
  /**
   * <code>.google.protobuf.Timestamp expires = 4;</code>
   * @return The expires.
   */
  @Override
  public com.google.protobuf.Timestamp getExpires() {
    return expires_ == null ? com.google.protobuf.Timestamp.getDefaultInstance() : expires_;
  }
  /**
   * <code>.google.protobuf.Timestamp expires = 4;</code>
   */
  @Override
  public com.google.protobuf.TimestampOrBuilder getExpiresOrBuilder() {
    return expires_ == null ? com.google.protobuf.Timestamp.getDefaultInstance() : expires_;
  }

  public static final int PAYLOAD_FIELD_NUMBER = 5;
  private com.google.protobuf.ByteString payload_ = com.google.protobuf.ByteString.EMPTY;
  /**
   * <code>bytes payload = 5;</code>
   * @return The payload.
   */
  @Override
  public com.google.protobuf.ByteString getPayload() {
    return payload_;
  }

  private byte memoizedIsInitialized = -1;
  @Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(id_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, id_);
    }
    for (int i = 0; i < recipients_.size(); i++) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, recipients_.getRaw(i));
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(sender_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 3, sender_);
    }
    if (expires_ != null) {
      output.writeMessage(4, getExpires());
    }
    if (!payload_.isEmpty()) {
      output.writeBytes(5, payload_);
    }
    getUnknownFields().writeTo(output);
  }

  @Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(id_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, id_);
    }
    {
      int dataSize = 0;
      for (int i = 0; i < recipients_.size(); i++) {
        dataSize += computeStringSizeNoTag(recipients_.getRaw(i));
      }
      size += dataSize;
      size += 1 * getRecipientsList().size();
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(sender_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, sender_);
    }
    if (expires_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(4, getExpires());
    }
    if (!payload_.isEmpty()) {
      size += com.google.protobuf.CodedOutputStream
        .computeBytesSize(5, payload_);
    }
    size += getUnknownFields().getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof DirectApplicationMessage)) {
      return super.equals(obj);
    }
    DirectApplicationMessage other = (DirectApplicationMessage) obj;

    if (!getId()
        .equals(other.getId())) return false;
    if (!getRecipientsList()
        .equals(other.getRecipientsList())) return false;
    if (!getSender()
        .equals(other.getSender())) return false;
    if (hasExpires() != other.hasExpires()) return false;
    if (hasExpires()) {
      if (!getExpires()
          .equals(other.getExpires())) return false;
    }
    if (!getPayload()
        .equals(other.getPayload())) return false;
    if (!getUnknownFields().equals(other.getUnknownFields())) return false;
    return true;
  }

  @Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + ID_FIELD_NUMBER;
    hash = (53 * hash) + getId().hashCode();
    if (getRecipientsCount() > 0) {
      hash = (37 * hash) + RECIPIENTS_FIELD_NUMBER;
      hash = (53 * hash) + getRecipientsList().hashCode();
    }
    hash = (37 * hash) + SENDER_FIELD_NUMBER;
    hash = (53 * hash) + getSender().hashCode();
    if (hasExpires()) {
      hash = (37 * hash) + EXPIRES_FIELD_NUMBER;
      hash = (53 * hash) + getExpires().hashCode();
    }
    hash = (37 * hash) + PAYLOAD_FIELD_NUMBER;
    hash = (53 * hash) + getPayload().hashCode();
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static DirectApplicationMessage parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static DirectApplicationMessage parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static DirectApplicationMessage parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static DirectApplicationMessage parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static DirectApplicationMessage parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static DirectApplicationMessage parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static DirectApplicationMessage parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static DirectApplicationMessage parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static DirectApplicationMessage parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static DirectApplicationMessage parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static DirectApplicationMessage parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static DirectApplicationMessage parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(DirectApplicationMessage prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @Override
  protected Builder newBuilderForType(
      BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code DirectApplicationMessage}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:DirectApplicationMessage)
          DirectApplicationMessageOrBuilder
  {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return MMTPMessages.internal_static_DirectApplicationMessage_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return MMTPMessages.internal_static_DirectApplicationMessage_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              DirectApplicationMessage.class, Builder.class);
    }

    // Construct using MMS.MMTP.DirectApplicationMessage.newBuilder()
    private Builder() {

    }

    private Builder(
        BuilderParent parent) {
      super(parent);

    }
    @Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      id_ = "";
      recipients_ =
          com.google.protobuf.LazyStringArrayList.emptyList();
      sender_ = "";
      expires_ = null;
      if (expiresBuilder_ != null) {
        expiresBuilder_.dispose();
        expiresBuilder_ = null;
      }
      payload_ = com.google.protobuf.ByteString.EMPTY;
      return this;
    }

    @Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return MMTPMessages.internal_static_DirectApplicationMessage_descriptor;
    }

    @Override
    public DirectApplicationMessage getDefaultInstanceForType() {
      return DirectApplicationMessage.getDefaultInstance();
    }

    @Override
    public DirectApplicationMessage build() {
      DirectApplicationMessage result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @Override
    public DirectApplicationMessage buildPartial() {
      DirectApplicationMessage result = new DirectApplicationMessage(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(DirectApplicationMessage result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.id_ = id_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        recipients_.makeImmutable();
        result.recipients_ = recipients_;
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.sender_ = sender_;
      }
      if (((from_bitField0_ & 0x00000008) != 0)) {
        result.expires_ = expiresBuilder_ == null
            ? expires_
            : expiresBuilder_.build();
      }
      if (((from_bitField0_ & 0x00000010) != 0)) {
        result.payload_ = payload_;
      }
    }

    @Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof DirectApplicationMessage) {
        return mergeFrom((DirectApplicationMessage)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(DirectApplicationMessage other) {
      if (other == DirectApplicationMessage.getDefaultInstance()) return this;
      if (!other.getId().isEmpty()) {
        id_ = other.id_;
        bitField0_ |= 0x00000001;
        onChanged();
      }
      if (!other.recipients_.isEmpty()) {
        if (recipients_.isEmpty()) {
          recipients_ = other.recipients_;
          bitField0_ |= 0x00000002;
        } else {
          ensureRecipientsIsMutable();
          recipients_.addAll(other.recipients_);
        }
        onChanged();
      }
      if (!other.getSender().isEmpty()) {
        sender_ = other.sender_;
        bitField0_ |= 0x00000004;
        onChanged();
      }
      if (other.hasExpires()) {
        mergeExpires(other.getExpires());
      }
      if (other.getPayload() != com.google.protobuf.ByteString.EMPTY) {
        setPayload(other.getPayload());
      }
      this.mergeUnknownFields(other.getUnknownFields());
      onChanged();
      return this;
    }

    @Override
    public final boolean isInitialized() {
      return true;
    }

    @Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      if (extensionRegistry == null) {
        throw new NullPointerException();
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
              id_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000001;
              break;
            } // case 10
            case 18: {
              String s = input.readStringRequireUtf8();
              ensureRecipientsIsMutable();
              recipients_.add(s);
              break;
            } // case 18
            case 26: {
              sender_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000004;
              break;
            } // case 26
            case 34: {
              input.readMessage(
                  getExpiresFieldBuilder().getBuilder(),
                  extensionRegistry);
              bitField0_ |= 0x00000008;
              break;
            } // case 34
            case 42: {
              payload_ = input.readBytes();
              bitField0_ |= 0x00000010;
              break;
            } // case 42
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

    private Object id_ = "";
    /**
     * <code>string id = 1;</code>
     * @return The id.
     */
    public String getId() {
      Object ref = id_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        id_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <code>string id = 1;</code>
     * @return The bytes for id.
     */
    public com.google.protobuf.ByteString
        getIdBytes() {
      Object ref = id_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        id_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string id = 1;</code>
     * @param value The id to set.
     * @return This builder for chaining.
     */
    public Builder setId(
        String value) {
      if (value == null) { throw new NullPointerException(); }
      id_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>string id = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearId() {
      id_ = getDefaultInstance().getId();
      bitField0_ = (bitField0_ & ~0x00000001);
      onChanged();
      return this;
    }
    /**
     * <code>string id = 1;</code>
     * @param value The bytes for id to set.
     * @return This builder for chaining.
     */
    public Builder setIdBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      id_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }

    private com.google.protobuf.LazyStringArrayList recipients_ =
        com.google.protobuf.LazyStringArrayList.emptyList();
    private void ensureRecipientsIsMutable() {
      if (!recipients_.isModifiable()) {
        recipients_ = new com.google.protobuf.LazyStringArrayList(recipients_);
      }
      bitField0_ |= 0x00000002;
    }
    /**
     * <code>repeated string recipients = 2;</code>
     * @return A list containing the recipients.
     */
    public com.google.protobuf.ProtocolStringList
        getRecipientsList() {
      recipients_.makeImmutable();
      return recipients_;
    }
    /**
     * <code>repeated string recipients = 2;</code>
     * @return The count of recipients.
     */
    public int getRecipientsCount() {
      return recipients_.size();
    }
    /**
     * <code>repeated string recipients = 2;</code>
     * @param index The index of the element to return.
     * @return The recipients at the given index.
     */
    public String getRecipients(int index) {
      return recipients_.get(index);
    }
    /**
     * <code>repeated string recipients = 2;</code>
     * @param index The index of the value to return.
     * @return The bytes of the recipients at the given index.
     */
    public com.google.protobuf.ByteString
        getRecipientsBytes(int index) {
      return recipients_.getByteString(index);
    }
    /**
     * <code>repeated string recipients = 2;</code>
     * @param index The index to set the value at.
     * @param value The recipients to set.
     * @return This builder for chaining.
     */
    public Builder setRecipients(
        int index, String value) {
      if (value == null) { throw new NullPointerException(); }
      ensureRecipientsIsMutable();
      recipients_.set(index, value);
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>repeated string recipients = 2;</code>
     * @param value The recipients to add.
     * @return This builder for chaining.
     */
    public Builder addRecipients(
        String value) {
      if (value == null) { throw new NullPointerException(); }
      ensureRecipientsIsMutable();
      recipients_.add(value);
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>repeated string recipients = 2;</code>
     * @param values The recipients to add.
     * @return This builder for chaining.
     */
    public Builder addAllRecipients(
        Iterable<String> values) {
      ensureRecipientsIsMutable();
      com.google.protobuf.AbstractMessageLite.Builder.addAll(
          values, recipients_);
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>repeated string recipients = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearRecipients() {
      recipients_ =
        com.google.protobuf.LazyStringArrayList.emptyList();
      bitField0_ = (bitField0_ & ~0x00000002);;
      onChanged();
      return this;
    }
    /**
     * <code>repeated string recipients = 2;</code>
     * @param value The bytes of the recipients to add.
     * @return This builder for chaining.
     */
    public Builder addRecipientsBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      ensureRecipientsIsMutable();
      recipients_.add(value);
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }

    private Object sender_ = "";
    /**
     * <code>string sender = 3;</code>
     * @return The sender.
     */
    public String getSender() {
      Object ref = sender_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        sender_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <code>string sender = 3;</code>
     * @return The bytes for sender.
     */
    public com.google.protobuf.ByteString
        getSenderBytes() {
      Object ref = sender_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        sender_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string sender = 3;</code>
     * @param value The sender to set.
     * @return This builder for chaining.
     */
    public Builder setSender(
        String value) {
      if (value == null) { throw new NullPointerException(); }
      sender_ = value;
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>string sender = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearSender() {
      sender_ = getDefaultInstance().getSender();
      bitField0_ = (bitField0_ & ~0x00000004);
      onChanged();
      return this;
    }
    /**
     * <code>string sender = 3;</code>
     * @param value The bytes for sender to set.
     * @return This builder for chaining.
     */
    public Builder setSenderBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      sender_ = value;
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }

    private com.google.protobuf.Timestamp expires_;
    private com.google.protobuf.SingleFieldBuilderV3<
        com.google.protobuf.Timestamp, com.google.protobuf.Timestamp.Builder, com.google.protobuf.TimestampOrBuilder> expiresBuilder_;
    /**
     * <code>.google.protobuf.Timestamp expires = 4;</code>
     * @return Whether the expires field is set.
     */
    public boolean hasExpires() {
      return ((bitField0_ & 0x00000008) != 0);
    }
    /**
     * <code>.google.protobuf.Timestamp expires = 4;</code>
     * @return The expires.
     */
    public com.google.protobuf.Timestamp getExpires() {
      if (expiresBuilder_ == null) {
        return expires_ == null ? com.google.protobuf.Timestamp.getDefaultInstance() : expires_;
      } else {
        return expiresBuilder_.getMessage();
      }
    }
    /**
     * <code>.google.protobuf.Timestamp expires = 4;</code>
     */
    public Builder setExpires(com.google.protobuf.Timestamp value) {
      if (expiresBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        expires_ = value;
      } else {
        expiresBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000008;
      onChanged();
      return this;
    }
    /**
     * <code>.google.protobuf.Timestamp expires = 4;</code>
     */
    public Builder setExpires(
        com.google.protobuf.Timestamp.Builder builderForValue) {
      if (expiresBuilder_ == null) {
        expires_ = builderForValue.build();
      } else {
        expiresBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000008;
      onChanged();
      return this;
    }
    /**
     * <code>.google.protobuf.Timestamp expires = 4;</code>
     */
    public Builder mergeExpires(com.google.protobuf.Timestamp value) {
      if (expiresBuilder_ == null) {
        if (((bitField0_ & 0x00000008) != 0) &&
          expires_ != null &&
          expires_ != com.google.protobuf.Timestamp.getDefaultInstance()) {
          getExpiresBuilder().mergeFrom(value);
        } else {
          expires_ = value;
        }
      } else {
        expiresBuilder_.mergeFrom(value);
      }
      bitField0_ |= 0x00000008;
      onChanged();
      return this;
    }
    /**
     * <code>.google.protobuf.Timestamp expires = 4;</code>
     */
    public Builder clearExpires() {
      bitField0_ = (bitField0_ & ~0x00000008);
      expires_ = null;
      if (expiresBuilder_ != null) {
        expiresBuilder_.dispose();
        expiresBuilder_ = null;
      }
      onChanged();
      return this;
    }
    /**
     * <code>.google.protobuf.Timestamp expires = 4;</code>
     */
    public com.google.protobuf.Timestamp.Builder getExpiresBuilder() {
      bitField0_ |= 0x00000008;
      onChanged();
      return getExpiresFieldBuilder().getBuilder();
    }
    /**
     * <code>.google.protobuf.Timestamp expires = 4;</code>
     */
    public com.google.protobuf.TimestampOrBuilder getExpiresOrBuilder() {
      if (expiresBuilder_ != null) {
        return expiresBuilder_.getMessageOrBuilder();
      } else {
        return expires_ == null ?
            com.google.protobuf.Timestamp.getDefaultInstance() : expires_;
      }
    }
    /**
     * <code>.google.protobuf.Timestamp expires = 4;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.google.protobuf.Timestamp, com.google.protobuf.Timestamp.Builder, com.google.protobuf.TimestampOrBuilder> 
        getExpiresFieldBuilder() {
      if (expiresBuilder_ == null) {
        expiresBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.google.protobuf.Timestamp, com.google.protobuf.Timestamp.Builder, com.google.protobuf.TimestampOrBuilder>(
                getExpires(),
                getParentForChildren(),
                isClean());
        expires_ = null;
      }
      return expiresBuilder_;
    }

    private com.google.protobuf.ByteString payload_ = com.google.protobuf.ByteString.EMPTY;
    /**
     * <code>bytes payload = 5;</code>
     * @return The payload.
     */
    @Override
    public com.google.protobuf.ByteString getPayload() {
      return payload_;
    }
    /**
     * <code>bytes payload = 5;</code>
     * @param value The payload to set.
     * @return This builder for chaining.
     */
    public Builder setPayload(com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      payload_ = value;
      bitField0_ |= 0x00000010;
      onChanged();
      return this;
    }
    /**
     * <code>bytes payload = 5;</code>
     * @return This builder for chaining.
     */
    public Builder clearPayload() {
      bitField0_ = (bitField0_ & ~0x00000010);
      payload_ = getDefaultInstance().getPayload();
      onChanged();
      return this;
    }
    @Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:DirectApplicationMessage)
  }

  // @@protoc_insertion_point(class_scope:DirectApplicationMessage)
  private static final DirectApplicationMessage DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new DirectApplicationMessage();
  }

  public static DirectApplicationMessage getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<DirectApplicationMessage>
      PARSER = new com.google.protobuf.AbstractParser<DirectApplicationMessage>() {
    @Override
    public DirectApplicationMessage parsePartialFrom(
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

  public static com.google.protobuf.Parser<DirectApplicationMessage> parser() {
    return PARSER;
  }

  @Override
  public com.google.protobuf.Parser<DirectApplicationMessage> getParserForType() {
    return PARSER;
  }

  @Override
  public DirectApplicationMessage getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

