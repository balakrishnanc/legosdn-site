package edu.duke.cs.legosdn.tools.cr.criu;

public final class CriuRPC {
  private CriuRPC() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  /**
   * Protobuf enum {@code criu_req_type}
   */
  public enum criu_req_type
      implements com.google.protobuf.ProtocolMessageEnum {
    /**
     * <code>EMPTY = 0;</code>
     */
    EMPTY(0, 0),
    /**
     * <code>DUMP = 1;</code>
     */
    DUMP(1, 1),
    /**
     * <code>RESTORE = 2;</code>
     */
    RESTORE(2, 2),
    /**
     * <code>CHECK = 3;</code>
     */
    CHECK(3, 3),
    /**
     * <code>PRE_DUMP = 4;</code>
     */
    PRE_DUMP(4, 4),
    /**
     * <code>PAGE_SERVER = 5;</code>
     */
    PAGE_SERVER(5, 5),
    /**
     * <code>NOTIFY = 6;</code>
     */
    NOTIFY(6, 6),
    /**
     * <code>CPUINFO_DUMP = 7;</code>
     */
    CPUINFO_DUMP(7, 7),
    /**
     * <code>CPUINFO_CHECK = 8;</code>
     */
    CPUINFO_CHECK(8, 8),
    ;

    /**
     * <code>EMPTY = 0;</code>
     */
    public static final int EMPTY_VALUE = 0;
    /**
     * <code>DUMP = 1;</code>
     */
    public static final int DUMP_VALUE = 1;
    /**
     * <code>RESTORE = 2;</code>
     */
    public static final int RESTORE_VALUE = 2;
    /**
     * <code>CHECK = 3;</code>
     */
    public static final int CHECK_VALUE = 3;
    /**
     * <code>PRE_DUMP = 4;</code>
     */
    public static final int PRE_DUMP_VALUE = 4;
    /**
     * <code>PAGE_SERVER = 5;</code>
     */
    public static final int PAGE_SERVER_VALUE = 5;
    /**
     * <code>NOTIFY = 6;</code>
     */
    public static final int NOTIFY_VALUE = 6;
    /**
     * <code>CPUINFO_DUMP = 7;</code>
     */
    public static final int CPUINFO_DUMP_VALUE = 7;
    /**
     * <code>CPUINFO_CHECK = 8;</code>
     */
    public static final int CPUINFO_CHECK_VALUE = 8;


    public final int getNumber() { return value; }

    public static criu_req_type valueOf(int value) {
      switch (value) {
        case 0: return EMPTY;
        case 1: return DUMP;
        case 2: return RESTORE;
        case 3: return CHECK;
        case 4: return PRE_DUMP;
        case 5: return PAGE_SERVER;
        case 6: return NOTIFY;
        case 7: return CPUINFO_DUMP;
        case 8: return CPUINFO_CHECK;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<criu_req_type>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<criu_req_type>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<criu_req_type>() {
            public criu_req_type findValueByNumber(int number) {
              return criu_req_type.valueOf(number);
            }
          };

    public final com.google.protobuf.Descriptors.EnumValueDescriptor
        getValueDescriptor() {
      return getDescriptor().getValues().get(index);
    }
    public final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptorForType() {
      return getDescriptor();
    }
    public static final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptor() {
      return CriuRPC.getDescriptor().getEnumTypes().get(0);
    }

    private static final criu_req_type[] VALUES = values();

    public static criu_req_type valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      return VALUES[desc.getIndex()];
    }

    private final int index;
    private final int value;

    private criu_req_type(int index, int value) {
      this.index = index;
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:criu_req_type)
  }

  public interface criu_page_server_infoOrBuilder extends
      // @@protoc_insertion_point(interface_extends:criu_page_server_info)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional string address = 1;</code>
     */
    boolean hasAddress();
    /**
     * <code>optional string address = 1;</code>
     */
    java.lang.String getAddress();
    /**
     * <code>optional string address = 1;</code>
     */
    com.google.protobuf.ByteString
        getAddressBytes();

    /**
     * <code>optional int32 port = 2;</code>
     */
    boolean hasPort();
    /**
     * <code>optional int32 port = 2;</code>
     */
    int getPort();

    /**
     * <code>optional int32 pid = 3;</code>
     */
    boolean hasPid();
    /**
     * <code>optional int32 pid = 3;</code>
     */
    int getPid();

    /**
     * <code>optional int32 fd = 4;</code>
     */
    boolean hasFd();
    /**
     * <code>optional int32 fd = 4;</code>
     */
    int getFd();
  }
  /**
   * Protobuf type {@code criu_page_server_info}
   */
  public static final class criu_page_server_info extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:criu_page_server_info)
      criu_page_server_infoOrBuilder {
    // Use criu_page_server_info.newBuilder() to construct.
    private criu_page_server_info(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private criu_page_server_info(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final criu_page_server_info defaultInstance;
    public static criu_page_server_info getDefaultInstance() {
      return defaultInstance;
    }

    public criu_page_server_info getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private criu_page_server_info(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000001;
              address_ = bs;
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              port_ = input.readInt32();
              break;
            }
            case 24: {
              bitField0_ |= 0x00000004;
              pid_ = input.readInt32();
              break;
            }
            case 32: {
              bitField0_ |= 0x00000008;
              fd_ = input.readInt32();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return CriuRPC.internal_static_criu_page_server_info_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return CriuRPC.internal_static_criu_page_server_info_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              CriuRPC.criu_page_server_info.class, CriuRPC.criu_page_server_info.Builder.class);
    }

    public static com.google.protobuf.Parser<criu_page_server_info> PARSER =
        new com.google.protobuf.AbstractParser<criu_page_server_info>() {
      public criu_page_server_info parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new criu_page_server_info(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<criu_page_server_info> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int ADDRESS_FIELD_NUMBER = 1;
    private java.lang.Object address_;
    /**
     * <code>optional string address = 1;</code>
     */
    public boolean hasAddress() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional string address = 1;</code>
     */
    public java.lang.String getAddress() {
      java.lang.Object ref = address_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          address_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string address = 1;</code>
     */
    public com.google.protobuf.ByteString
        getAddressBytes() {
      java.lang.Object ref = address_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        address_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int PORT_FIELD_NUMBER = 2;
    private int port_;
    /**
     * <code>optional int32 port = 2;</code>
     */
    public boolean hasPort() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional int32 port = 2;</code>
     */
    public int getPort() {
      return port_;
    }

    public static final int PID_FIELD_NUMBER = 3;
    private int pid_;
    /**
     * <code>optional int32 pid = 3;</code>
     */
    public boolean hasPid() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>optional int32 pid = 3;</code>
     */
    public int getPid() {
      return pid_;
    }

    public static final int FD_FIELD_NUMBER = 4;
    private int fd_;
    /**
     * <code>optional int32 fd = 4;</code>
     */
    public boolean hasFd() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    /**
     * <code>optional int32 fd = 4;</code>
     */
    public int getFd() {
      return fd_;
    }

    private void initFields() {
      address_ = "";
      port_ = 0;
      pid_ = 0;
      fd_ = 0;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, getAddressBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeInt32(2, port_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeInt32(3, pid_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeInt32(4, fd_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, getAddressBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(2, port_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(3, pid_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(4, fd_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CriuRPC.criu_page_server_info parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CriuRPC.criu_page_server_info parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CriuRPC.criu_page_server_info parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CriuRPC.criu_page_server_info parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CriuRPC.criu_page_server_info parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CriuRPC.criu_page_server_info parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static CriuRPC.criu_page_server_info parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static CriuRPC.criu_page_server_info parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static CriuRPC.criu_page_server_info parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CriuRPC.criu_page_server_info parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(CriuRPC.criu_page_server_info prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code criu_page_server_info}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:criu_page_server_info)
        CriuRPC.criu_page_server_infoOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return CriuRPC.internal_static_criu_page_server_info_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return CriuRPC.internal_static_criu_page_server_info_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                CriuRPC.criu_page_server_info.class, CriuRPC.criu_page_server_info.Builder.class);
      }

      // Construct using Rpc.criu_page_server_info.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        address_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        port_ = 0;
        bitField0_ = (bitField0_ & ~0x00000002);
        pid_ = 0;
        bitField0_ = (bitField0_ & ~0x00000004);
        fd_ = 0;
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return CriuRPC.internal_static_criu_page_server_info_descriptor;
      }

      public CriuRPC.criu_page_server_info getDefaultInstanceForType() {
        return CriuRPC.criu_page_server_info.getDefaultInstance();
      }

      public CriuRPC.criu_page_server_info build() {
        CriuRPC.criu_page_server_info result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CriuRPC.criu_page_server_info buildPartial() {
        CriuRPC.criu_page_server_info result = new CriuRPC.criu_page_server_info(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.address_ = address_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.port_ = port_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.pid_ = pid_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.fd_ = fd_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CriuRPC.criu_page_server_info) {
          return mergeFrom((CriuRPC.criu_page_server_info)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CriuRPC.criu_page_server_info other) {
        if (other == CriuRPC.criu_page_server_info.getDefaultInstance()) return this;
        if (other.hasAddress()) {
          bitField0_ |= 0x00000001;
          address_ = other.address_;
          onChanged();
        }
        if (other.hasPort()) {
          setPort(other.getPort());
        }
        if (other.hasPid()) {
          setPid(other.getPid());
        }
        if (other.hasFd()) {
          setFd(other.getFd());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        CriuRPC.criu_page_server_info parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CriuRPC.criu_page_server_info) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.lang.Object address_ = "";
      /**
       * <code>optional string address = 1;</code>
       */
      public boolean hasAddress() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional string address = 1;</code>
       */
      public java.lang.String getAddress() {
        java.lang.Object ref = address_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            address_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string address = 1;</code>
       */
      public com.google.protobuf.ByteString
          getAddressBytes() {
        java.lang.Object ref = address_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          address_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string address = 1;</code>
       */
      public Builder setAddress(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        address_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string address = 1;</code>
       */
      public Builder clearAddress() {
        bitField0_ = (bitField0_ & ~0x00000001);
        address_ = getDefaultInstance().getAddress();
        onChanged();
        return this;
      }
      /**
       * <code>optional string address = 1;</code>
       */
      public Builder setAddressBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        address_ = value;
        onChanged();
        return this;
      }

      private int port_ ;
      /**
       * <code>optional int32 port = 2;</code>
       */
      public boolean hasPort() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional int32 port = 2;</code>
       */
      public int getPort() {
        return port_;
      }
      /**
       * <code>optional int32 port = 2;</code>
       */
      public Builder setPort(int value) {
        bitField0_ |= 0x00000002;
        port_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional int32 port = 2;</code>
       */
      public Builder clearPort() {
        bitField0_ = (bitField0_ & ~0x00000002);
        port_ = 0;
        onChanged();
        return this;
      }

      private int pid_ ;
      /**
       * <code>optional int32 pid = 3;</code>
       */
      public boolean hasPid() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      /**
       * <code>optional int32 pid = 3;</code>
       */
      public int getPid() {
        return pid_;
      }
      /**
       * <code>optional int32 pid = 3;</code>
       */
      public Builder setPid(int value) {
        bitField0_ |= 0x00000004;
        pid_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional int32 pid = 3;</code>
       */
      public Builder clearPid() {
        bitField0_ = (bitField0_ & ~0x00000004);
        pid_ = 0;
        onChanged();
        return this;
      }

      private int fd_ ;
      /**
       * <code>optional int32 fd = 4;</code>
       */
      public boolean hasFd() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      /**
       * <code>optional int32 fd = 4;</code>
       */
      public int getFd() {
        return fd_;
      }
      /**
       * <code>optional int32 fd = 4;</code>
       */
      public Builder setFd(int value) {
        bitField0_ |= 0x00000008;
        fd_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional int32 fd = 4;</code>
       */
      public Builder clearFd() {
        bitField0_ = (bitField0_ & ~0x00000008);
        fd_ = 0;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:criu_page_server_info)
    }

    static {
      defaultInstance = new criu_page_server_info(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:criu_page_server_info)
  }

  public interface criu_veth_pairOrBuilder extends
      // @@protoc_insertion_point(interface_extends:criu_veth_pair)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>required string if_in = 1;</code>
     */
    boolean hasIfIn();
    /**
     * <code>required string if_in = 1;</code>
     */
    java.lang.String getIfIn();
    /**
     * <code>required string if_in = 1;</code>
     */
    com.google.protobuf.ByteString
        getIfInBytes();

    /**
     * <code>required string if_out = 2;</code>
     */
    boolean hasIfOut();
    /**
     * <code>required string if_out = 2;</code>
     */
    java.lang.String getIfOut();
    /**
     * <code>required string if_out = 2;</code>
     */
    com.google.protobuf.ByteString
        getIfOutBytes();
  }
  /**
   * Protobuf type {@code criu_veth_pair}
   */
  public static final class criu_veth_pair extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:criu_veth_pair)
      criu_veth_pairOrBuilder {
    // Use criu_veth_pair.newBuilder() to construct.
    private criu_veth_pair(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private criu_veth_pair(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final criu_veth_pair defaultInstance;
    public static criu_veth_pair getDefaultInstance() {
      return defaultInstance;
    }

    public criu_veth_pair getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private criu_veth_pair(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000001;
              ifIn_ = bs;
              break;
            }
            case 18: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000002;
              ifOut_ = bs;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return CriuRPC.internal_static_criu_veth_pair_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return CriuRPC.internal_static_criu_veth_pair_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              CriuRPC.criu_veth_pair.class, CriuRPC.criu_veth_pair.Builder.class);
    }

    public static com.google.protobuf.Parser<criu_veth_pair> PARSER =
        new com.google.protobuf.AbstractParser<criu_veth_pair>() {
      public criu_veth_pair parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new criu_veth_pair(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<criu_veth_pair> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int IF_IN_FIELD_NUMBER = 1;
    private java.lang.Object ifIn_;
    /**
     * <code>required string if_in = 1;</code>
     */
    public boolean hasIfIn() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>required string if_in = 1;</code>
     */
    public java.lang.String getIfIn() {
      java.lang.Object ref = ifIn_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          ifIn_ = s;
        }
        return s;
      }
    }
    /**
     * <code>required string if_in = 1;</code>
     */
    public com.google.protobuf.ByteString
        getIfInBytes() {
      java.lang.Object ref = ifIn_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        ifIn_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int IF_OUT_FIELD_NUMBER = 2;
    private java.lang.Object ifOut_;
    /**
     * <code>required string if_out = 2;</code>
     */
    public boolean hasIfOut() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>required string if_out = 2;</code>
     */
    public java.lang.String getIfOut() {
      java.lang.Object ref = ifOut_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          ifOut_ = s;
        }
        return s;
      }
    }
    /**
     * <code>required string if_out = 2;</code>
     */
    public com.google.protobuf.ByteString
        getIfOutBytes() {
      java.lang.Object ref = ifOut_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        ifOut_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private void initFields() {
      ifIn_ = "";
      ifOut_ = "";
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      if (!hasIfIn()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasIfOut()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, getIfInBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getIfOutBytes());
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, getIfInBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getIfOutBytes());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CriuRPC.criu_veth_pair parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CriuRPC.criu_veth_pair parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CriuRPC.criu_veth_pair parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CriuRPC.criu_veth_pair parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CriuRPC.criu_veth_pair parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CriuRPC.criu_veth_pair parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static CriuRPC.criu_veth_pair parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static CriuRPC.criu_veth_pair parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static CriuRPC.criu_veth_pair parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CriuRPC.criu_veth_pair parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(CriuRPC.criu_veth_pair prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code criu_veth_pair}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:criu_veth_pair)
        CriuRPC.criu_veth_pairOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return CriuRPC.internal_static_criu_veth_pair_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return CriuRPC.internal_static_criu_veth_pair_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                CriuRPC.criu_veth_pair.class, CriuRPC.criu_veth_pair.Builder.class);
      }

      // Construct using Rpc.criu_veth_pair.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        ifIn_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        ifOut_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return CriuRPC.internal_static_criu_veth_pair_descriptor;
      }

      public CriuRPC.criu_veth_pair getDefaultInstanceForType() {
        return CriuRPC.criu_veth_pair.getDefaultInstance();
      }

      public CriuRPC.criu_veth_pair build() {
        CriuRPC.criu_veth_pair result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CriuRPC.criu_veth_pair buildPartial() {
        CriuRPC.criu_veth_pair result = new CriuRPC.criu_veth_pair(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.ifIn_ = ifIn_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.ifOut_ = ifOut_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CriuRPC.criu_veth_pair) {
          return mergeFrom((CriuRPC.criu_veth_pair)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CriuRPC.criu_veth_pair other) {
        if (other == CriuRPC.criu_veth_pair.getDefaultInstance()) return this;
        if (other.hasIfIn()) {
          bitField0_ |= 0x00000001;
          ifIn_ = other.ifIn_;
          onChanged();
        }
        if (other.hasIfOut()) {
          bitField0_ |= 0x00000002;
          ifOut_ = other.ifOut_;
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        if (!hasIfIn()) {
          
          return false;
        }
        if (!hasIfOut()) {
          
          return false;
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        CriuRPC.criu_veth_pair parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CriuRPC.criu_veth_pair) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.lang.Object ifIn_ = "";
      /**
       * <code>required string if_in = 1;</code>
       */
      public boolean hasIfIn() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>required string if_in = 1;</code>
       */
      public java.lang.String getIfIn() {
        java.lang.Object ref = ifIn_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            ifIn_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>required string if_in = 1;</code>
       */
      public com.google.protobuf.ByteString
          getIfInBytes() {
        java.lang.Object ref = ifIn_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          ifIn_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>required string if_in = 1;</code>
       */
      public Builder setIfIn(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        ifIn_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required string if_in = 1;</code>
       */
      public Builder clearIfIn() {
        bitField0_ = (bitField0_ & ~0x00000001);
        ifIn_ = getDefaultInstance().getIfIn();
        onChanged();
        return this;
      }
      /**
       * <code>required string if_in = 1;</code>
       */
      public Builder setIfInBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        ifIn_ = value;
        onChanged();
        return this;
      }

      private java.lang.Object ifOut_ = "";
      /**
       * <code>required string if_out = 2;</code>
       */
      public boolean hasIfOut() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>required string if_out = 2;</code>
       */
      public java.lang.String getIfOut() {
        java.lang.Object ref = ifOut_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            ifOut_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>required string if_out = 2;</code>
       */
      public com.google.protobuf.ByteString
          getIfOutBytes() {
        java.lang.Object ref = ifOut_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          ifOut_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>required string if_out = 2;</code>
       */
      public Builder setIfOut(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        ifOut_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required string if_out = 2;</code>
       */
      public Builder clearIfOut() {
        bitField0_ = (bitField0_ & ~0x00000002);
        ifOut_ = getDefaultInstance().getIfOut();
        onChanged();
        return this;
      }
      /**
       * <code>required string if_out = 2;</code>
       */
      public Builder setIfOutBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        ifOut_ = value;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:criu_veth_pair)
    }

    static {
      defaultInstance = new criu_veth_pair(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:criu_veth_pair)
  }

  public interface ext_mount_mapOrBuilder extends
      // @@protoc_insertion_point(interface_extends:ext_mount_map)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>required string key = 1;</code>
     */
    boolean hasKey();
    /**
     * <code>required string key = 1;</code>
     */
    java.lang.String getKey();
    /**
     * <code>required string key = 1;</code>
     */
    com.google.protobuf.ByteString
        getKeyBytes();

    /**
     * <code>required string val = 2;</code>
     */
    boolean hasVal();
    /**
     * <code>required string val = 2;</code>
     */
    java.lang.String getVal();
    /**
     * <code>required string val = 2;</code>
     */
    com.google.protobuf.ByteString
        getValBytes();
  }
  /**
   * Protobuf type {@code ext_mount_map}
   */
  public static final class ext_mount_map extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:ext_mount_map)
      ext_mount_mapOrBuilder {
    // Use ext_mount_map.newBuilder() to construct.
    private ext_mount_map(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private ext_mount_map(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final ext_mount_map defaultInstance;
    public static ext_mount_map getDefaultInstance() {
      return defaultInstance;
    }

    public ext_mount_map getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private ext_mount_map(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000001;
              key_ = bs;
              break;
            }
            case 18: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000002;
              val_ = bs;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return CriuRPC.internal_static_ext_mount_map_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return CriuRPC.internal_static_ext_mount_map_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              CriuRPC.ext_mount_map.class, CriuRPC.ext_mount_map.Builder.class);
    }

    public static com.google.protobuf.Parser<ext_mount_map> PARSER =
        new com.google.protobuf.AbstractParser<ext_mount_map>() {
      public ext_mount_map parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new ext_mount_map(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<ext_mount_map> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int KEY_FIELD_NUMBER = 1;
    private java.lang.Object key_;
    /**
     * <code>required string key = 1;</code>
     */
    public boolean hasKey() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>required string key = 1;</code>
     */
    public java.lang.String getKey() {
      java.lang.Object ref = key_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          key_ = s;
        }
        return s;
      }
    }
    /**
     * <code>required string key = 1;</code>
     */
    public com.google.protobuf.ByteString
        getKeyBytes() {
      java.lang.Object ref = key_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        key_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int VAL_FIELD_NUMBER = 2;
    private java.lang.Object val_;
    /**
     * <code>required string val = 2;</code>
     */
    public boolean hasVal() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>required string val = 2;</code>
     */
    public java.lang.String getVal() {
      java.lang.Object ref = val_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          val_ = s;
        }
        return s;
      }
    }
    /**
     * <code>required string val = 2;</code>
     */
    public com.google.protobuf.ByteString
        getValBytes() {
      java.lang.Object ref = val_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        val_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private void initFields() {
      key_ = "";
      val_ = "";
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      if (!hasKey()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasVal()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, getKeyBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getValBytes());
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, getKeyBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getValBytes());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CriuRPC.ext_mount_map parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CriuRPC.ext_mount_map parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CriuRPC.ext_mount_map parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CriuRPC.ext_mount_map parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CriuRPC.ext_mount_map parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CriuRPC.ext_mount_map parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static CriuRPC.ext_mount_map parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static CriuRPC.ext_mount_map parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static CriuRPC.ext_mount_map parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CriuRPC.ext_mount_map parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(CriuRPC.ext_mount_map prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code ext_mount_map}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:ext_mount_map)
        CriuRPC.ext_mount_mapOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return CriuRPC.internal_static_ext_mount_map_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return CriuRPC.internal_static_ext_mount_map_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                CriuRPC.ext_mount_map.class, CriuRPC.ext_mount_map.Builder.class);
      }

      // Construct using Rpc.ext_mount_map.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        key_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        val_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return CriuRPC.internal_static_ext_mount_map_descriptor;
      }

      public CriuRPC.ext_mount_map getDefaultInstanceForType() {
        return CriuRPC.ext_mount_map.getDefaultInstance();
      }

      public CriuRPC.ext_mount_map build() {
        CriuRPC.ext_mount_map result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CriuRPC.ext_mount_map buildPartial() {
        CriuRPC.ext_mount_map result = new CriuRPC.ext_mount_map(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.key_ = key_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.val_ = val_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CriuRPC.ext_mount_map) {
          return mergeFrom((CriuRPC.ext_mount_map)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CriuRPC.ext_mount_map other) {
        if (other == CriuRPC.ext_mount_map.getDefaultInstance()) return this;
        if (other.hasKey()) {
          bitField0_ |= 0x00000001;
          key_ = other.key_;
          onChanged();
        }
        if (other.hasVal()) {
          bitField0_ |= 0x00000002;
          val_ = other.val_;
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        if (!hasKey()) {
          
          return false;
        }
        if (!hasVal()) {
          
          return false;
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        CriuRPC.ext_mount_map parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CriuRPC.ext_mount_map) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.lang.Object key_ = "";
      /**
       * <code>required string key = 1;</code>
       */
      public boolean hasKey() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>required string key = 1;</code>
       */
      public java.lang.String getKey() {
        java.lang.Object ref = key_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            key_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>required string key = 1;</code>
       */
      public com.google.protobuf.ByteString
          getKeyBytes() {
        java.lang.Object ref = key_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          key_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>required string key = 1;</code>
       */
      public Builder setKey(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        key_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required string key = 1;</code>
       */
      public Builder clearKey() {
        bitField0_ = (bitField0_ & ~0x00000001);
        key_ = getDefaultInstance().getKey();
        onChanged();
        return this;
      }
      /**
       * <code>required string key = 1;</code>
       */
      public Builder setKeyBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        key_ = value;
        onChanged();
        return this;
      }

      private java.lang.Object val_ = "";
      /**
       * <code>required string val = 2;</code>
       */
      public boolean hasVal() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>required string val = 2;</code>
       */
      public java.lang.String getVal() {
        java.lang.Object ref = val_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            val_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>required string val = 2;</code>
       */
      public com.google.protobuf.ByteString
          getValBytes() {
        java.lang.Object ref = val_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          val_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>required string val = 2;</code>
       */
      public Builder setVal(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        val_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required string val = 2;</code>
       */
      public Builder clearVal() {
        bitField0_ = (bitField0_ & ~0x00000002);
        val_ = getDefaultInstance().getVal();
        onChanged();
        return this;
      }
      /**
       * <code>required string val = 2;</code>
       */
      public Builder setValBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        val_ = value;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:ext_mount_map)
    }

    static {
      defaultInstance = new ext_mount_map(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:ext_mount_map)
  }

  public interface cgroup_rootOrBuilder extends
      // @@protoc_insertion_point(interface_extends:cgroup_root)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional string ctrl = 1;</code>
     */
    boolean hasCtrl();
    /**
     * <code>optional string ctrl = 1;</code>
     */
    java.lang.String getCtrl();
    /**
     * <code>optional string ctrl = 1;</code>
     */
    com.google.protobuf.ByteString
        getCtrlBytes();

    /**
     * <code>required string path = 2;</code>
     */
    boolean hasPath();
    /**
     * <code>required string path = 2;</code>
     */
    java.lang.String getPath();
    /**
     * <code>required string path = 2;</code>
     */
    com.google.protobuf.ByteString
        getPathBytes();
  }
  /**
   * Protobuf type {@code cgroup_root}
   */
  public static final class cgroup_root extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:cgroup_root)
      cgroup_rootOrBuilder {
    // Use cgroup_root.newBuilder() to construct.
    private cgroup_root(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private cgroup_root(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final cgroup_root defaultInstance;
    public static cgroup_root getDefaultInstance() {
      return defaultInstance;
    }

    public cgroup_root getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private cgroup_root(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000001;
              ctrl_ = bs;
              break;
            }
            case 18: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000002;
              path_ = bs;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return CriuRPC.internal_static_cgroup_root_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return CriuRPC.internal_static_cgroup_root_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              CriuRPC.cgroup_root.class, CriuRPC.cgroup_root.Builder.class);
    }

    public static com.google.protobuf.Parser<cgroup_root> PARSER =
        new com.google.protobuf.AbstractParser<cgroup_root>() {
      public cgroup_root parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new cgroup_root(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<cgroup_root> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int CTRL_FIELD_NUMBER = 1;
    private java.lang.Object ctrl_;
    /**
     * <code>optional string ctrl = 1;</code>
     */
    public boolean hasCtrl() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional string ctrl = 1;</code>
     */
    public java.lang.String getCtrl() {
      java.lang.Object ref = ctrl_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          ctrl_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string ctrl = 1;</code>
     */
    public com.google.protobuf.ByteString
        getCtrlBytes() {
      java.lang.Object ref = ctrl_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        ctrl_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int PATH_FIELD_NUMBER = 2;
    private java.lang.Object path_;
    /**
     * <code>required string path = 2;</code>
     */
    public boolean hasPath() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>required string path = 2;</code>
     */
    public java.lang.String getPath() {
      java.lang.Object ref = path_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          path_ = s;
        }
        return s;
      }
    }
    /**
     * <code>required string path = 2;</code>
     */
    public com.google.protobuf.ByteString
        getPathBytes() {
      java.lang.Object ref = path_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        path_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private void initFields() {
      ctrl_ = "";
      path_ = "";
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      if (!hasPath()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, getCtrlBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getPathBytes());
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, getCtrlBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getPathBytes());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CriuRPC.cgroup_root parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CriuRPC.cgroup_root parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CriuRPC.cgroup_root parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CriuRPC.cgroup_root parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CriuRPC.cgroup_root parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CriuRPC.cgroup_root parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static CriuRPC.cgroup_root parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static CriuRPC.cgroup_root parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static CriuRPC.cgroup_root parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CriuRPC.cgroup_root parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(CriuRPC.cgroup_root prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code cgroup_root}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:cgroup_root)
        CriuRPC.cgroup_rootOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return CriuRPC.internal_static_cgroup_root_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return CriuRPC.internal_static_cgroup_root_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                CriuRPC.cgroup_root.class, CriuRPC.cgroup_root.Builder.class);
      }

      // Construct using Rpc.cgroup_root.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        ctrl_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        path_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return CriuRPC.internal_static_cgroup_root_descriptor;
      }

      public CriuRPC.cgroup_root getDefaultInstanceForType() {
        return CriuRPC.cgroup_root.getDefaultInstance();
      }

      public CriuRPC.cgroup_root build() {
        CriuRPC.cgroup_root result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CriuRPC.cgroup_root buildPartial() {
        CriuRPC.cgroup_root result = new CriuRPC.cgroup_root(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.ctrl_ = ctrl_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.path_ = path_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CriuRPC.cgroup_root) {
          return mergeFrom((CriuRPC.cgroup_root)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CriuRPC.cgroup_root other) {
        if (other == CriuRPC.cgroup_root.getDefaultInstance()) return this;
        if (other.hasCtrl()) {
          bitField0_ |= 0x00000001;
          ctrl_ = other.ctrl_;
          onChanged();
        }
        if (other.hasPath()) {
          bitField0_ |= 0x00000002;
          path_ = other.path_;
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        if (!hasPath()) {
          
          return false;
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        CriuRPC.cgroup_root parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CriuRPC.cgroup_root) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.lang.Object ctrl_ = "";
      /**
       * <code>optional string ctrl = 1;</code>
       */
      public boolean hasCtrl() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional string ctrl = 1;</code>
       */
      public java.lang.String getCtrl() {
        java.lang.Object ref = ctrl_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            ctrl_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string ctrl = 1;</code>
       */
      public com.google.protobuf.ByteString
          getCtrlBytes() {
        java.lang.Object ref = ctrl_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          ctrl_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string ctrl = 1;</code>
       */
      public Builder setCtrl(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        ctrl_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string ctrl = 1;</code>
       */
      public Builder clearCtrl() {
        bitField0_ = (bitField0_ & ~0x00000001);
        ctrl_ = getDefaultInstance().getCtrl();
        onChanged();
        return this;
      }
      /**
       * <code>optional string ctrl = 1;</code>
       */
      public Builder setCtrlBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        ctrl_ = value;
        onChanged();
        return this;
      }

      private java.lang.Object path_ = "";
      /**
       * <code>required string path = 2;</code>
       */
      public boolean hasPath() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>required string path = 2;</code>
       */
      public java.lang.String getPath() {
        java.lang.Object ref = path_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            path_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>required string path = 2;</code>
       */
      public com.google.protobuf.ByteString
          getPathBytes() {
        java.lang.Object ref = path_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          path_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>required string path = 2;</code>
       */
      public Builder setPath(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        path_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required string path = 2;</code>
       */
      public Builder clearPath() {
        bitField0_ = (bitField0_ & ~0x00000002);
        path_ = getDefaultInstance().getPath();
        onChanged();
        return this;
      }
      /**
       * <code>required string path = 2;</code>
       */
      public Builder setPathBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        path_ = value;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:cgroup_root)
    }

    static {
      defaultInstance = new cgroup_root(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:cgroup_root)
  }

  public interface criu_optsOrBuilder extends
      // @@protoc_insertion_point(interface_extends:criu_opts)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>required int32 images_dir_fd = 1;</code>
     */
    boolean hasImagesDirFd();
    /**
     * <code>required int32 images_dir_fd = 1;</code>
     */
    int getImagesDirFd();

    /**
     * <code>optional int32 pid = 2;</code>
     *
     * <pre>
     * if not set on dump, will dump requesting process 
     * </pre>
     */
    boolean hasPid();
    /**
     * <code>optional int32 pid = 2;</code>
     *
     * <pre>
     * if not set on dump, will dump requesting process 
     * </pre>
     */
    int getPid();

    /**
     * <code>optional bool leave_running = 3;</code>
     */
    boolean hasLeaveRunning();
    /**
     * <code>optional bool leave_running = 3;</code>
     */
    boolean getLeaveRunning();

    /**
     * <code>optional bool ext_unix_sk = 4;</code>
     */
    boolean hasExtUnixSk();
    /**
     * <code>optional bool ext_unix_sk = 4;</code>
     */
    boolean getExtUnixSk();

    /**
     * <code>optional bool tcp_established = 5;</code>
     */
    boolean hasTcpEstablished();
    /**
     * <code>optional bool tcp_established = 5;</code>
     */
    boolean getTcpEstablished();

    /**
     * <code>optional bool evasive_devices = 6;</code>
     */
    boolean hasEvasiveDevices();
    /**
     * <code>optional bool evasive_devices = 6;</code>
     */
    boolean getEvasiveDevices();

    /**
     * <code>optional bool shell_job = 7;</code>
     */
    boolean hasShellJob();
    /**
     * <code>optional bool shell_job = 7;</code>
     */
    boolean getShellJob();

    /**
     * <code>optional bool file_locks = 8;</code>
     */
    boolean hasFileLocks();
    /**
     * <code>optional bool file_locks = 8;</code>
     */
    boolean getFileLocks();

    /**
     * <code>optional int32 log_level = 9 [default = 2];</code>
     */
    boolean hasLogLevel();
    /**
     * <code>optional int32 log_level = 9 [default = 2];</code>
     */
    int getLogLevel();

    /**
     * <code>optional string log_file = 10;</code>
     *
     * <pre>
     * No subdirs are allowed. Consider using work-dir 
     * </pre>
     */
    boolean hasLogFile();
    /**
     * <code>optional string log_file = 10;</code>
     *
     * <pre>
     * No subdirs are allowed. Consider using work-dir 
     * </pre>
     */
    java.lang.String getLogFile();
    /**
     * <code>optional string log_file = 10;</code>
     *
     * <pre>
     * No subdirs are allowed. Consider using work-dir 
     * </pre>
     */
    com.google.protobuf.ByteString
        getLogFileBytes();

    /**
     * <code>optional .criu_page_server_info ps = 11;</code>
     */
    boolean hasPs();
    /**
     * <code>optional .criu_page_server_info ps = 11;</code>
     */
    CriuRPC.criu_page_server_info getPs();
    /**
     * <code>optional .criu_page_server_info ps = 11;</code>
     */
    CriuRPC.criu_page_server_infoOrBuilder getPsOrBuilder();

    /**
     * <code>optional bool notify_scripts = 12;</code>
     */
    boolean hasNotifyScripts();
    /**
     * <code>optional bool notify_scripts = 12;</code>
     */
    boolean getNotifyScripts();

    /**
     * <code>optional string root = 13;</code>
     */
    boolean hasRoot();
    /**
     * <code>optional string root = 13;</code>
     */
    java.lang.String getRoot();
    /**
     * <code>optional string root = 13;</code>
     */
    com.google.protobuf.ByteString
        getRootBytes();

    /**
     * <code>optional string parent_img = 14;</code>
     */
    boolean hasParentImg();
    /**
     * <code>optional string parent_img = 14;</code>
     */
    java.lang.String getParentImg();
    /**
     * <code>optional string parent_img = 14;</code>
     */
    com.google.protobuf.ByteString
        getParentImgBytes();

    /**
     * <code>optional bool track_mem = 15;</code>
     */
    boolean hasTrackMem();
    /**
     * <code>optional bool track_mem = 15;</code>
     */
    boolean getTrackMem();

    /**
     * <code>optional bool auto_dedup = 16;</code>
     */
    boolean hasAutoDedup();
    /**
     * <code>optional bool auto_dedup = 16;</code>
     */
    boolean getAutoDedup();

    /**
     * <code>optional int32 work_dir_fd = 17;</code>
     */
    boolean hasWorkDirFd();
    /**
     * <code>optional int32 work_dir_fd = 17;</code>
     */
    int getWorkDirFd();

    /**
     * <code>optional bool link_remap = 18;</code>
     */
    boolean hasLinkRemap();
    /**
     * <code>optional bool link_remap = 18;</code>
     */
    boolean getLinkRemap();

    /**
     * <code>repeated .criu_veth_pair veths = 19;</code>
     */
    java.util.List<CriuRPC.criu_veth_pair>
        getVethsList();
    /**
     * <code>repeated .criu_veth_pair veths = 19;</code>
     */
    CriuRPC.criu_veth_pair getVeths(int index);
    /**
     * <code>repeated .criu_veth_pair veths = 19;</code>
     */
    int getVethsCount();
    /**
     * <code>repeated .criu_veth_pair veths = 19;</code>
     */
    java.util.List<? extends CriuRPC.criu_veth_pairOrBuilder>
        getVethsOrBuilderList();
    /**
     * <code>repeated .criu_veth_pair veths = 19;</code>
     */
    CriuRPC.criu_veth_pairOrBuilder getVethsOrBuilder(
        int index);

    /**
     * <code>optional uint32 cpu_cap = 20 [default = 4294967295];</code>
     */
    boolean hasCpuCap();
    /**
     * <code>optional uint32 cpu_cap = 20 [default = 4294967295];</code>
     */
    int getCpuCap();

    /**
     * <code>optional bool force_irmap = 21;</code>
     */
    boolean hasForceIrmap();
    /**
     * <code>optional bool force_irmap = 21;</code>
     */
    boolean getForceIrmap();

    /**
     * <code>repeated string exec_cmd = 22;</code>
     */
    com.google.protobuf.ProtocolStringList
        getExecCmdList();
    /**
     * <code>repeated string exec_cmd = 22;</code>
     */
    int getExecCmdCount();
    /**
     * <code>repeated string exec_cmd = 22;</code>
     */
    java.lang.String getExecCmd(int index);
    /**
     * <code>repeated string exec_cmd = 22;</code>
     */
    com.google.protobuf.ByteString
        getExecCmdBytes(int index);

    /**
     * <code>repeated .ext_mount_map ext_mnt = 23;</code>
     */
    java.util.List<CriuRPC.ext_mount_map>
        getExtMntList();
    /**
     * <code>repeated .ext_mount_map ext_mnt = 23;</code>
     */
    CriuRPC.ext_mount_map getExtMnt(int index);
    /**
     * <code>repeated .ext_mount_map ext_mnt = 23;</code>
     */
    int getExtMntCount();
    /**
     * <code>repeated .ext_mount_map ext_mnt = 23;</code>
     */
    java.util.List<? extends CriuRPC.ext_mount_mapOrBuilder>
        getExtMntOrBuilderList();
    /**
     * <code>repeated .ext_mount_map ext_mnt = 23;</code>
     */
    CriuRPC.ext_mount_mapOrBuilder getExtMntOrBuilder(
        int index);

    /**
     * <code>optional bool manage_cgroups = 24;</code>
     */
    boolean hasManageCgroups();
    /**
     * <code>optional bool manage_cgroups = 24;</code>
     */
    boolean getManageCgroups();

    /**
     * <code>repeated .cgroup_root cg_root = 25;</code>
     */
    java.util.List<CriuRPC.cgroup_root>
        getCgRootList();
    /**
     * <code>repeated .cgroup_root cg_root = 25;</code>
     */
    CriuRPC.cgroup_root getCgRoot(int index);
    /**
     * <code>repeated .cgroup_root cg_root = 25;</code>
     */
    int getCgRootCount();
    /**
     * <code>repeated .cgroup_root cg_root = 25;</code>
     */
    java.util.List<? extends CriuRPC.cgroup_rootOrBuilder>
        getCgRootOrBuilderList();
    /**
     * <code>repeated .cgroup_root cg_root = 25;</code>
     */
    CriuRPC.cgroup_rootOrBuilder getCgRootOrBuilder(
        int index);

    /**
     * <code>optional bool rst_sibling = 26;</code>
     *
     * <pre>
     * swrk only 
     * </pre>
     */
    boolean hasRstSibling();
    /**
     * <code>optional bool rst_sibling = 26;</code>
     *
     * <pre>
     * swrk only 
     * </pre>
     */
    boolean getRstSibling();

    /**
     * <code>required int32 app_num = 27;</code>
     */
    boolean hasAppNum();
    /**
     * <code>required int32 app_num = 27;</code>
     */
    int getAppNum();
  }
  /**
   * Protobuf type {@code criu_opts}
   */
  public static final class criu_opts extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:criu_opts)
      criu_optsOrBuilder {
    // Use criu_opts.newBuilder() to construct.
    private criu_opts(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private criu_opts(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final criu_opts defaultInstance;
    public static criu_opts getDefaultInstance() {
      return defaultInstance;
    }

    public criu_opts getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private criu_opts(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 8: {
              bitField0_ |= 0x00000001;
              imagesDirFd_ = input.readInt32();
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              pid_ = input.readInt32();
              break;
            }
            case 24: {
              bitField0_ |= 0x00000004;
              leaveRunning_ = input.readBool();
              break;
            }
            case 32: {
              bitField0_ |= 0x00000008;
              extUnixSk_ = input.readBool();
              break;
            }
            case 40: {
              bitField0_ |= 0x00000010;
              tcpEstablished_ = input.readBool();
              break;
            }
            case 48: {
              bitField0_ |= 0x00000020;
              evasiveDevices_ = input.readBool();
              break;
            }
            case 56: {
              bitField0_ |= 0x00000040;
              shellJob_ = input.readBool();
              break;
            }
            case 64: {
              bitField0_ |= 0x00000080;
              fileLocks_ = input.readBool();
              break;
            }
            case 72: {
              bitField0_ |= 0x00000100;
              logLevel_ = input.readInt32();
              break;
            }
            case 82: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000200;
              logFile_ = bs;
              break;
            }
            case 90: {
              CriuRPC.criu_page_server_info.Builder subBuilder = null;
              if (((bitField0_ & 0x00000400) == 0x00000400)) {
                subBuilder = ps_.toBuilder();
              }
              ps_ = input.readMessage(CriuRPC.criu_page_server_info.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(ps_);
                ps_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000400;
              break;
            }
            case 96: {
              bitField0_ |= 0x00000800;
              notifyScripts_ = input.readBool();
              break;
            }
            case 106: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00001000;
              root_ = bs;
              break;
            }
            case 114: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00002000;
              parentImg_ = bs;
              break;
            }
            case 120: {
              bitField0_ |= 0x00004000;
              trackMem_ = input.readBool();
              break;
            }
            case 128: {
              bitField0_ |= 0x00008000;
              autoDedup_ = input.readBool();
              break;
            }
            case 136: {
              bitField0_ |= 0x00010000;
              workDirFd_ = input.readInt32();
              break;
            }
            case 144: {
              bitField0_ |= 0x00020000;
              linkRemap_ = input.readBool();
              break;
            }
            case 154: {
              if (!((mutable_bitField0_ & 0x00040000) == 0x00040000)) {
                veths_ = new java.util.ArrayList<CriuRPC.criu_veth_pair>();
                mutable_bitField0_ |= 0x00040000;
              }
              veths_.add(input.readMessage(CriuRPC.criu_veth_pair.PARSER, extensionRegistry));
              break;
            }
            case 160: {
              bitField0_ |= 0x00040000;
              cpuCap_ = input.readUInt32();
              break;
            }
            case 168: {
              bitField0_ |= 0x00080000;
              forceIrmap_ = input.readBool();
              break;
            }
            case 178: {
              com.google.protobuf.ByteString bs = input.readBytes();
              if (!((mutable_bitField0_ & 0x00200000) == 0x00200000)) {
                execCmd_ = new com.google.protobuf.LazyStringArrayList();
                mutable_bitField0_ |= 0x00200000;
              }
              execCmd_.add(bs);
              break;
            }
            case 186: {
              if (!((mutable_bitField0_ & 0x00400000) == 0x00400000)) {
                extMnt_ = new java.util.ArrayList<CriuRPC.ext_mount_map>();
                mutable_bitField0_ |= 0x00400000;
              }
              extMnt_.add(input.readMessage(CriuRPC.ext_mount_map.PARSER, extensionRegistry));
              break;
            }
            case 192: {
              bitField0_ |= 0x00100000;
              manageCgroups_ = input.readBool();
              break;
            }
            case 202: {
              if (!((mutable_bitField0_ & 0x01000000) == 0x01000000)) {
                cgRoot_ = new java.util.ArrayList<CriuRPC.cgroup_root>();
                mutable_bitField0_ |= 0x01000000;
              }
              cgRoot_.add(input.readMessage(CriuRPC.cgroup_root.PARSER, extensionRegistry));
              break;
            }
            case 208: {
              bitField0_ |= 0x00200000;
              rstSibling_ = input.readBool();
              break;
            }
            case 216: {
              bitField0_ |= 0x00400000;
              appNum_ = input.readInt32();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        if (((mutable_bitField0_ & 0x00040000) == 0x00040000)) {
          veths_ = java.util.Collections.unmodifiableList(veths_);
        }
        if (((mutable_bitField0_ & 0x00200000) == 0x00200000)) {
          execCmd_ = execCmd_.getUnmodifiableView();
        }
        if (((mutable_bitField0_ & 0x00400000) == 0x00400000)) {
          extMnt_ = java.util.Collections.unmodifiableList(extMnt_);
        }
        if (((mutable_bitField0_ & 0x01000000) == 0x01000000)) {
          cgRoot_ = java.util.Collections.unmodifiableList(cgRoot_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return CriuRPC.internal_static_criu_opts_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return CriuRPC.internal_static_criu_opts_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              CriuRPC.criu_opts.class, CriuRPC.criu_opts.Builder.class);
    }

    public static com.google.protobuf.Parser<criu_opts> PARSER =
        new com.google.protobuf.AbstractParser<criu_opts>() {
      public criu_opts parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new criu_opts(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<criu_opts> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int IMAGES_DIR_FD_FIELD_NUMBER = 1;
    private int imagesDirFd_;
    /**
     * <code>required int32 images_dir_fd = 1;</code>
     */
    public boolean hasImagesDirFd() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>required int32 images_dir_fd = 1;</code>
     */
    public int getImagesDirFd() {
      return imagesDirFd_;
    }

    public static final int PID_FIELD_NUMBER = 2;
    private int pid_;
    /**
     * <code>optional int32 pid = 2;</code>
     *
     * <pre>
     * if not set on dump, will dump requesting process 
     * </pre>
     */
    public boolean hasPid() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional int32 pid = 2;</code>
     *
     * <pre>
     * if not set on dump, will dump requesting process 
     * </pre>
     */
    public int getPid() {
      return pid_;
    }

    public static final int LEAVE_RUNNING_FIELD_NUMBER = 3;
    private boolean leaveRunning_;
    /**
     * <code>optional bool leave_running = 3;</code>
     */
    public boolean hasLeaveRunning() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>optional bool leave_running = 3;</code>
     */
    public boolean getLeaveRunning() {
      return leaveRunning_;
    }

    public static final int EXT_UNIX_SK_FIELD_NUMBER = 4;
    private boolean extUnixSk_;
    /**
     * <code>optional bool ext_unix_sk = 4;</code>
     */
    public boolean hasExtUnixSk() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    /**
     * <code>optional bool ext_unix_sk = 4;</code>
     */
    public boolean getExtUnixSk() {
      return extUnixSk_;
    }

    public static final int TCP_ESTABLISHED_FIELD_NUMBER = 5;
    private boolean tcpEstablished_;
    /**
     * <code>optional bool tcp_established = 5;</code>
     */
    public boolean hasTcpEstablished() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    /**
     * <code>optional bool tcp_established = 5;</code>
     */
    public boolean getTcpEstablished() {
      return tcpEstablished_;
    }

    public static final int EVASIVE_DEVICES_FIELD_NUMBER = 6;
    private boolean evasiveDevices_;
    /**
     * <code>optional bool evasive_devices = 6;</code>
     */
    public boolean hasEvasiveDevices() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }
    /**
     * <code>optional bool evasive_devices = 6;</code>
     */
    public boolean getEvasiveDevices() {
      return evasiveDevices_;
    }

    public static final int SHELL_JOB_FIELD_NUMBER = 7;
    private boolean shellJob_;
    /**
     * <code>optional bool shell_job = 7;</code>
     */
    public boolean hasShellJob() {
      return ((bitField0_ & 0x00000040) == 0x00000040);
    }
    /**
     * <code>optional bool shell_job = 7;</code>
     */
    public boolean getShellJob() {
      return shellJob_;
    }

    public static final int FILE_LOCKS_FIELD_NUMBER = 8;
    private boolean fileLocks_;
    /**
     * <code>optional bool file_locks = 8;</code>
     */
    public boolean hasFileLocks() {
      return ((bitField0_ & 0x00000080) == 0x00000080);
    }
    /**
     * <code>optional bool file_locks = 8;</code>
     */
    public boolean getFileLocks() {
      return fileLocks_;
    }

    public static final int LOG_LEVEL_FIELD_NUMBER = 9;
    private int logLevel_;
    /**
     * <code>optional int32 log_level = 9 [default = 2];</code>
     */
    public boolean hasLogLevel() {
      return ((bitField0_ & 0x00000100) == 0x00000100);
    }
    /**
     * <code>optional int32 log_level = 9 [default = 2];</code>
     */
    public int getLogLevel() {
      return logLevel_;
    }

    public static final int LOG_FILE_FIELD_NUMBER = 10;
    private java.lang.Object logFile_;
    /**
     * <code>optional string log_file = 10;</code>
     *
     * <pre>
     * No subdirs are allowed. Consider using work-dir 
     * </pre>
     */
    public boolean hasLogFile() {
      return ((bitField0_ & 0x00000200) == 0x00000200);
    }
    /**
     * <code>optional string log_file = 10;</code>
     *
     * <pre>
     * No subdirs are allowed. Consider using work-dir 
     * </pre>
     */
    public java.lang.String getLogFile() {
      java.lang.Object ref = logFile_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          logFile_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string log_file = 10;</code>
     *
     * <pre>
     * No subdirs are allowed. Consider using work-dir 
     * </pre>
     */
    public com.google.protobuf.ByteString
        getLogFileBytes() {
      java.lang.Object ref = logFile_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        logFile_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int PS_FIELD_NUMBER = 11;
    private CriuRPC.criu_page_server_info ps_;

    /**
     * <code>optional .criu_page_server_info ps = 11;</code>
     */
    public boolean hasPs() {
      return ((bitField0_ & 0x00000400) == 0x00000400);
    }

    /**
     * <code>optional .criu_page_server_info ps = 11;</code>
     */
    public CriuRPC.criu_page_server_info getPs() {
      return ps_;
    }

    /**
     * <code>optional .criu_page_server_info ps = 11;</code>
     */
    public CriuRPC.criu_page_server_infoOrBuilder getPsOrBuilder() {
      return ps_;
    }

    public static final int NOTIFY_SCRIPTS_FIELD_NUMBER = 12;
    private boolean notifyScripts_;

    /**
     * <code>optional bool notify_scripts = 12;</code>
     */
    public boolean hasNotifyScripts() {
      return ((bitField0_ & 0x00000800) == 0x00000800);
    }

    /**
     * <code>optional bool notify_scripts = 12;</code>
     */
    public boolean getNotifyScripts() {
      return notifyScripts_;
    }

    public static final int ROOT_FIELD_NUMBER = 13;
    private java.lang.Object root_;

    /**
     * <code>optional string root = 13;</code>
     */
    public boolean hasRoot() {
      return ((bitField0_ & 0x00001000) == 0x00001000);
    }

    /**
     * <code>optional string root = 13;</code>
     */
    public java.lang.String getRoot() {
      java.lang.Object ref = root_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs =
                (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          root_ = s;
        }
        return s;
      }
    }

    /**
     * <code>optional string root = 13;</code>
     */
    public com.google.protobuf.ByteString
    getRootBytes() {
      java.lang.Object ref = root_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b =
                com.google.protobuf.ByteString.copyFromUtf8(
                        (java.lang.String) ref);
        root_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int PARENT_IMG_FIELD_NUMBER = 14;
    private java.lang.Object parentImg_;

    /**
     * <code>optional string parent_img = 14;</code>
     */
    public boolean hasParentImg() {
      return ((bitField0_ & 0x00002000) == 0x00002000);
    }

    /**
     * <code>optional string parent_img = 14;</code>
     */
    public java.lang.String getParentImg() {
      java.lang.Object ref = parentImg_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs =
                (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          parentImg_ = s;
        }
        return s;
      }
    }

    /**
     * <code>optional string parent_img = 14;</code>
     */
    public com.google.protobuf.ByteString
    getParentImgBytes() {
      java.lang.Object ref = parentImg_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b =
                com.google.protobuf.ByteString.copyFromUtf8(
                        (java.lang.String) ref);
        parentImg_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int TRACK_MEM_FIELD_NUMBER = 15;
    private boolean trackMem_;

    /**
     * <code>optional bool track_mem = 15;</code>
     */
    public boolean hasTrackMem() {
      return ((bitField0_ & 0x00004000) == 0x00004000);
    }

    /**
     * <code>optional bool track_mem = 15;</code>
     */
    public boolean getTrackMem() {
      return trackMem_;
    }

    public static final int AUTO_DEDUP_FIELD_NUMBER = 16;
    private boolean autoDedup_;

    /**
     * <code>optional bool auto_dedup = 16;</code>
     */
    public boolean hasAutoDedup() {
      return ((bitField0_ & 0x00008000) == 0x00008000);
    }

    /**
     * <code>optional bool auto_dedup = 16;</code>
     */
    public boolean getAutoDedup() {
      return autoDedup_;
    }

    public static final int WORK_DIR_FD_FIELD_NUMBER = 17;
    private int workDirFd_;

    /**
     * <code>optional int32 work_dir_fd = 17;</code>
     */
    public boolean hasWorkDirFd() {
      return ((bitField0_ & 0x00010000) == 0x00010000);
    }

    /**
     * <code>optional int32 work_dir_fd = 17;</code>
     */
    public int getWorkDirFd() {
      return workDirFd_;
    }

    public static final int LINK_REMAP_FIELD_NUMBER = 18;
    private boolean linkRemap_;

    /**
     * <code>optional bool link_remap = 18;</code>
     */
    public boolean hasLinkRemap() {
      return ((bitField0_ & 0x00020000) == 0x00020000);
    }

    /**
     * <code>optional bool link_remap = 18;</code>
     */
    public boolean getLinkRemap() {
      return linkRemap_;
    }

    public static final int VETHS_FIELD_NUMBER = 19;
    private java.util.List<CriuRPC.criu_veth_pair> veths_;

    /**
     * <code>repeated .criu_veth_pair veths = 19;</code>
     */
    public java.util.List<CriuRPC.criu_veth_pair> getVethsList() {
      return veths_;
    }

    /**
     * <code>repeated .criu_veth_pair veths = 19;</code>
     */
    public java.util.List<? extends CriuRPC.criu_veth_pairOrBuilder>
    getVethsOrBuilderList() {
      return veths_;
    }

    /**
     * <code>repeated .criu_veth_pair veths = 19;</code>
     */
    public int getVethsCount() {
      return veths_.size();
    }

    /**
     * <code>repeated .criu_veth_pair veths = 19;</code>
     */
    public CriuRPC.criu_veth_pair getVeths(int index) {
      return veths_.get(index);
    }

    /**
     * <code>repeated .criu_veth_pair veths = 19;</code>
     */
    public CriuRPC.criu_veth_pairOrBuilder getVethsOrBuilder(
            int index) {
      return veths_.get(index);
    }

    public static final int CPU_CAP_FIELD_NUMBER = 20;
    private int cpuCap_;

    /**
     * <code>optional uint32 cpu_cap = 20 [default = 4294967295];</code>
     */
    public boolean hasCpuCap() {
      return ((bitField0_ & 0x00040000) == 0x00040000);
    }

    /**
     * <code>optional uint32 cpu_cap = 20 [default = 4294967295];</code>
     */
    public int getCpuCap() {
      return cpuCap_;
    }

    public static final int FORCE_IRMAP_FIELD_NUMBER = 21;
    private boolean forceIrmap_;

    /**
     * <code>optional bool force_irmap = 21;</code>
     */
    public boolean hasForceIrmap() {
      return ((bitField0_ & 0x00080000) == 0x00080000);
    }

    /**
     * <code>optional bool force_irmap = 21;</code>
     */
    public boolean getForceIrmap() {
      return forceIrmap_;
    }

    public static final int EXEC_CMD_FIELD_NUMBER = 22;
    private com.google.protobuf.LazyStringList execCmd_;

    /**
     * <code>repeated string exec_cmd = 22;</code>
     */
    public com.google.protobuf.ProtocolStringList
    getExecCmdList() {
      return execCmd_;
    }

    /**
     * <code>repeated string exec_cmd = 22;</code>
     */
    public int getExecCmdCount() {
      return execCmd_.size();
    }

    /**
     * <code>repeated string exec_cmd = 22;</code>
     */
    public java.lang.String getExecCmd(int index) {
      return execCmd_.get(index);
    }

    /**
     * <code>repeated string exec_cmd = 22;</code>
     */
    public com.google.protobuf.ByteString
    getExecCmdBytes(int index) {
      return execCmd_.getByteString(index);
    }

    public static final int EXT_MNT_FIELD_NUMBER = 23;
    private java.util.List<CriuRPC.ext_mount_map> extMnt_;

    /**
     * <code>repeated .ext_mount_map ext_mnt = 23;</code>
     */
    public java.util.List<CriuRPC.ext_mount_map> getExtMntList() {
      return extMnt_;
    }

    /**
     * <code>repeated .ext_mount_map ext_mnt = 23;</code>
     */
    public java.util.List<? extends CriuRPC.ext_mount_mapOrBuilder>
    getExtMntOrBuilderList() {
      return extMnt_;
    }

    /**
     * <code>repeated .ext_mount_map ext_mnt = 23;</code>
     */
    public int getExtMntCount() {
      return extMnt_.size();
    }

    /**
     * <code>repeated .ext_mount_map ext_mnt = 23;</code>
     */
    public CriuRPC.ext_mount_map getExtMnt(int index) {
      return extMnt_.get(index);
    }

    /**
     * <code>repeated .ext_mount_map ext_mnt = 23;</code>
     */
    public CriuRPC.ext_mount_mapOrBuilder getExtMntOrBuilder(
            int index) {
      return extMnt_.get(index);
    }

    public static final int MANAGE_CGROUPS_FIELD_NUMBER = 24;
    private boolean manageCgroups_;

    /**
     * <code>optional bool manage_cgroups = 24;</code>
     */
    public boolean hasManageCgroups() {
      return ((bitField0_ & 0x00100000) == 0x00100000);
    }

    /**
     * <code>optional bool manage_cgroups = 24;</code>
     */
    public boolean getManageCgroups() {
      return manageCgroups_;
    }

    public static final int CG_ROOT_FIELD_NUMBER = 25;
    private java.util.List<CriuRPC.cgroup_root> cgRoot_;

    /**
     * <code>repeated .cgroup_root cg_root = 25;</code>
     */
    public java.util.List<CriuRPC.cgroup_root> getCgRootList() {
      return cgRoot_;
    }

    /**
     * <code>repeated .cgroup_root cg_root = 25;</code>
     */
    public java.util.List<? extends CriuRPC.cgroup_rootOrBuilder>
    getCgRootOrBuilderList() {
      return cgRoot_;
    }

    /**
     * <code>repeated .cgroup_root cg_root = 25;</code>
     */
    public int getCgRootCount() {
      return cgRoot_.size();
    }

    /**
     * <code>repeated .cgroup_root cg_root = 25;</code>
     */
    public CriuRPC.cgroup_root getCgRoot(int index) {
      return cgRoot_.get(index);
    }

    /**
     * <code>repeated .cgroup_root cg_root = 25;</code>
     */
    public CriuRPC.cgroup_rootOrBuilder getCgRootOrBuilder(
            int index) {
      return cgRoot_.get(index);
    }

    public static final int RST_SIBLING_FIELD_NUMBER = 26;
    private boolean rstSibling_;

    /**
     * <code>optional bool rst_sibling = 26;</code>
     *
     * <pre>
     * swrk only 
     * </pre>
     */
    public boolean hasRstSibling() {
      return ((bitField0_ & 0x00200000) == 0x00200000);
    }

    /**
     * <code>optional bool rst_sibling = 26;</code>
     *
     * <pre>
     * swrk only 
     * </pre>
     */
    public boolean getRstSibling() {
      return rstSibling_;
    }

    public static final int APP_NUM_FIELD_NUMBER = 27;
    private int appNum_;

    /**
     * <code>required int32 app_num = 27;</code>
     */
    public boolean hasAppNum() {
      return ((bitField0_ & 0x00400000) == 0x00400000);
    }

    /**
     * <code>required int32 app_num = 27;</code>
     */
    public int getAppNum() {
      return appNum_;
    }

    private void initFields() {
      imagesDirFd_ = 0;
      pid_ = 0;
      leaveRunning_ = false;
      extUnixSk_ = false;
      tcpEstablished_ = false;
      evasiveDevices_ = false;
      shellJob_ = false;
      fileLocks_ = false;
      logLevel_ = 2;
      logFile_ = "";
      ps_ = CriuRPC.criu_page_server_info.getDefaultInstance();
      notifyScripts_ = false;
      root_ = "";
      parentImg_ = "";
      trackMem_ = false;
      autoDedup_ = false;
      workDirFd_ = 0;
      linkRemap_ = false;
      veths_ = java.util.Collections.emptyList();
      cpuCap_ = -1;
      forceIrmap_ = false;
      execCmd_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      extMnt_ = java.util.Collections.emptyList();
      manageCgroups_ = false;
      cgRoot_ = java.util.Collections.emptyList();
      rstSibling_ = false;
      appNum_ = 0;
    }

    private byte memoizedIsInitialized = -1;

    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      if (!hasImagesDirFd()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasAppNum()) {
        memoizedIsInitialized = 0;
        return false;
      }
      for (int i = 0; i < getVethsCount(); i++) {
        if (!getVeths(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      for (int i = 0; i < getExtMntCount(); i++) {
        if (!getExtMnt(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      for (int i = 0; i < getCgRootCount(); i++) {
        if (!getCgRoot(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
            throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeInt32(1, imagesDirFd_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeInt32(2, pid_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeBool(3, leaveRunning_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeBool(4, extUnixSk_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeBool(5, tcpEstablished_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeBool(6, evasiveDevices_);
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        output.writeBool(7, shellJob_);
      }
      if (((bitField0_ & 0x00000080) == 0x00000080)) {
        output.writeBool(8, fileLocks_);
      }
      if (((bitField0_ & 0x00000100) == 0x00000100)) {
        output.writeInt32(9, logLevel_);
      }
      if (((bitField0_ & 0x00000200) == 0x00000200)) {
        output.writeBytes(10, getLogFileBytes());
      }
      if (((bitField0_ & 0x00000400) == 0x00000400)) {
        output.writeMessage(11, ps_);
      }
      if (((bitField0_ & 0x00000800) == 0x00000800)) {
        output.writeBool(12, notifyScripts_);
      }
      if (((bitField0_ & 0x00001000) == 0x00001000)) {
        output.writeBytes(13, getRootBytes());
      }
      if (((bitField0_ & 0x00002000) == 0x00002000)) {
        output.writeBytes(14, getParentImgBytes());
      }
      if (((bitField0_ & 0x00004000) == 0x00004000)) {
        output.writeBool(15, trackMem_);
      }
      if (((bitField0_ & 0x00008000) == 0x00008000)) {
        output.writeBool(16, autoDedup_);
      }
      if (((bitField0_ & 0x00010000) == 0x00010000)) {
        output.writeInt32(17, workDirFd_);
      }
      if (((bitField0_ & 0x00020000) == 0x00020000)) {
        output.writeBool(18, linkRemap_);
      }
      for (int i = 0; i < veths_.size(); i++) {
        output.writeMessage(19, veths_.get(i));
      }
      if (((bitField0_ & 0x00040000) == 0x00040000)) {
        output.writeUInt32(20, cpuCap_);
      }
      if (((bitField0_ & 0x00080000) == 0x00080000)) {
        output.writeBool(21, forceIrmap_);
      }
      for (int i = 0; i < execCmd_.size(); i++) {
        output.writeBytes(22, execCmd_.getByteString(i));
      }
      for (int i = 0; i < extMnt_.size(); i++) {
        output.writeMessage(23, extMnt_.get(i));
      }
      if (((bitField0_ & 0x00100000) == 0x00100000)) {
        output.writeBool(24, manageCgroups_);
      }
      for (int i = 0; i < cgRoot_.size(); i++) {
        output.writeMessage(25, cgRoot_.get(i));
      }
      if (((bitField0_ & 0x00200000) == 0x00200000)) {
        output.writeBool(26, rstSibling_);
      }
      if (((bitField0_ & 0x00400000) == 0x00400000)) {
        output.writeInt32(27, appNum_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;

    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
                .computeInt32Size(1, imagesDirFd_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
                .computeInt32Size(2, pid_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
                .computeBoolSize(3, leaveRunning_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
                .computeBoolSize(4, extUnixSk_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
                .computeBoolSize(5, tcpEstablished_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
                .computeBoolSize(6, evasiveDevices_);
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        size += com.google.protobuf.CodedOutputStream
                .computeBoolSize(7, shellJob_);
      }
      if (((bitField0_ & 0x00000080) == 0x00000080)) {
        size += com.google.protobuf.CodedOutputStream
                .computeBoolSize(8, fileLocks_);
      }
      if (((bitField0_ & 0x00000100) == 0x00000100)) {
        size += com.google.protobuf.CodedOutputStream
                .computeInt32Size(9, logLevel_);
      }
      if (((bitField0_ & 0x00000200) == 0x00000200)) {
        size += com.google.protobuf.CodedOutputStream
                .computeBytesSize(10, getLogFileBytes());
      }
      if (((bitField0_ & 0x00000400) == 0x00000400)) {
        size += com.google.protobuf.CodedOutputStream
                .computeMessageSize(11, ps_);
      }
      if (((bitField0_ & 0x00000800) == 0x00000800)) {
        size += com.google.protobuf.CodedOutputStream
                .computeBoolSize(12, notifyScripts_);
      }
      if (((bitField0_ & 0x00001000) == 0x00001000)) {
        size += com.google.protobuf.CodedOutputStream
                .computeBytesSize(13, getRootBytes());
      }
      if (((bitField0_ & 0x00002000) == 0x00002000)) {
        size += com.google.protobuf.CodedOutputStream
                .computeBytesSize(14, getParentImgBytes());
      }
      if (((bitField0_ & 0x00004000) == 0x00004000)) {
        size += com.google.protobuf.CodedOutputStream
                .computeBoolSize(15, trackMem_);
      }
      if (((bitField0_ & 0x00008000) == 0x00008000)) {
        size += com.google.protobuf.CodedOutputStream
                .computeBoolSize(16, autoDedup_);
      }
      if (((bitField0_ & 0x00010000) == 0x00010000)) {
        size += com.google.protobuf.CodedOutputStream
                .computeInt32Size(17, workDirFd_);
      }
      if (((bitField0_ & 0x00020000) == 0x00020000)) {
        size += com.google.protobuf.CodedOutputStream
                .computeBoolSize(18, linkRemap_);
      }
      for (int i = 0; i < veths_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
                .computeMessageSize(19, veths_.get(i));
      }
      if (((bitField0_ & 0x00040000) == 0x00040000)) {
        size += com.google.protobuf.CodedOutputStream
                .computeUInt32Size(20, cpuCap_);
      }
      if (((bitField0_ & 0x00080000) == 0x00080000)) {
        size += com.google.protobuf.CodedOutputStream
                .computeBoolSize(21, forceIrmap_);
      }
      {
        int dataSize = 0;
        for (int i = 0; i < execCmd_.size(); i++) {
          dataSize += com.google.protobuf.CodedOutputStream
                  .computeBytesSizeNoTag(execCmd_.getByteString(i));
        }
        size += dataSize;
        size += 2 * getExecCmdList().size();
      }
      for (int i = 0; i < extMnt_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
                .computeMessageSize(23, extMnt_.get(i));
      }
      if (((bitField0_ & 0x00100000) == 0x00100000)) {
        size += com.google.protobuf.CodedOutputStream
                .computeBoolSize(24, manageCgroups_);
      }
      for (int i = 0; i < cgRoot_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
                .computeMessageSize(25, cgRoot_.get(i));
      }
      if (((bitField0_ & 0x00200000) == 0x00200000)) {
        size += com.google.protobuf.CodedOutputStream
                .computeBoolSize(26, rstSibling_);
      }
      if (((bitField0_ & 0x00400000) == 0x00400000)) {
        size += com.google.protobuf.CodedOutputStream
                .computeInt32Size(27, appNum_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;

    @java.lang.Override
    protected java.lang.Object writeReplace()
            throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CriuRPC.criu_opts parseFrom(
            com.google.protobuf.ByteString data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static CriuRPC.criu_opts parseFrom(
            com.google.protobuf.ByteString data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static CriuRPC.criu_opts parseFrom(byte[] data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static CriuRPC.criu_opts parseFrom(
            byte[] data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static CriuRPC.criu_opts parseFrom(java.io.InputStream input)
            throws java.io.IOException {
      return PARSER.parseFrom(input);
    }

    public static CriuRPC.criu_opts parseFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static CriuRPC.criu_opts parseDelimitedFrom(java.io.InputStream input)
            throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }

    public static CriuRPC.criu_opts parseDelimitedFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }

    public static CriuRPC.criu_opts parseFrom(
            com.google.protobuf.CodedInputStream input)
            throws java.io.IOException {
      return PARSER.parseFrom(input);
    }

    public static CriuRPC.criu_opts parseFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() {
      return Builder.create();
    }

    public Builder newBuilderForType() {
      return newBuilder();
    }

    public static Builder newBuilder(CriuRPC.criu_opts prototype) {
      return newBuilder().mergeFrom(prototype);
    }

    public Builder toBuilder() {
      return newBuilder(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
            com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }

    /**
     * Protobuf type {@code criu_opts}
     */
    public static final class Builder extends
                                      com.google.protobuf.GeneratedMessage.Builder<Builder> implements
                                                                                            // @@protoc_insertion_point(builder_implements:criu_opts)
                                                                                                    CriuRPC.criu_optsOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
        return CriuRPC.internal_static_criu_opts_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internalGetFieldAccessorTable() {
        return CriuRPC.internal_static_criu_opts_fieldAccessorTable
                .ensureFieldAccessorsInitialized(
                        CriuRPC.criu_opts.class, CriuRPC.criu_opts.Builder.class);
      }

      // Construct using Rpc.criu_opts.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
              com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }

      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getPsFieldBuilder();
          getVethsFieldBuilder();
          getExtMntFieldBuilder();
          getCgRootFieldBuilder();
        }
      }

      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        imagesDirFd_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        pid_ = 0;
        bitField0_ = (bitField0_ & ~0x00000002);
        leaveRunning_ = false;
        bitField0_ = (bitField0_ & ~0x00000004);
        extUnixSk_ = false;
        bitField0_ = (bitField0_ & ~0x00000008);
        tcpEstablished_ = false;
        bitField0_ = (bitField0_ & ~0x00000010);
        evasiveDevices_ = false;
        bitField0_ = (bitField0_ & ~0x00000020);
        shellJob_ = false;
        bitField0_ = (bitField0_ & ~0x00000040);
        fileLocks_ = false;
        bitField0_ = (bitField0_ & ~0x00000080);
        logLevel_ = 2;
        bitField0_ = (bitField0_ & ~0x00000100);
        logFile_ = "";
        bitField0_ = (bitField0_ & ~0x00000200);
        if (psBuilder_ == null) {
          ps_ = CriuRPC.criu_page_server_info.getDefaultInstance();
        } else {
          psBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000400);
        notifyScripts_ = false;
        bitField0_ = (bitField0_ & ~0x00000800);
        root_ = "";
        bitField0_ = (bitField0_ & ~0x00001000);
        parentImg_ = "";
        bitField0_ = (bitField0_ & ~0x00002000);
        trackMem_ = false;
        bitField0_ = (bitField0_ & ~0x00004000);
        autoDedup_ = false;
        bitField0_ = (bitField0_ & ~0x00008000);
        workDirFd_ = 0;
        bitField0_ = (bitField0_ & ~0x00010000);
        linkRemap_ = false;
        bitField0_ = (bitField0_ & ~0x00020000);
        if (vethsBuilder_ == null) {
          veths_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00040000);
        } else {
          vethsBuilder_.clear();
        }
        cpuCap_ = -1;
        bitField0_ = (bitField0_ & ~0x00080000);
        forceIrmap_ = false;
        bitField0_ = (bitField0_ & ~0x00100000);
        execCmd_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00200000);
        if (extMntBuilder_ == null) {
          extMnt_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00400000);
        } else {
          extMntBuilder_.clear();
        }
        manageCgroups_ = false;
        bitField0_ = (bitField0_ & ~0x00800000);
        if (cgRootBuilder_ == null) {
          cgRoot_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x01000000);
        } else {
          cgRootBuilder_.clear();
        }
        rstSibling_ = false;
        bitField0_ = (bitField0_ & ~0x02000000);
        appNum_ = 0;
        bitField0_ = (bitField0_ & ~0x04000000);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
      getDescriptorForType() {
        return CriuRPC.internal_static_criu_opts_descriptor;
      }

      public CriuRPC.criu_opts getDefaultInstanceForType() {
        return CriuRPC.criu_opts.getDefaultInstance();
      }

      public CriuRPC.criu_opts build() {
        CriuRPC.criu_opts result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CriuRPC.criu_opts buildPartial() {
        CriuRPC.criu_opts result = new CriuRPC.criu_opts(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.imagesDirFd_ = imagesDirFd_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.pid_ = pid_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.leaveRunning_ = leaveRunning_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.extUnixSk_ = extUnixSk_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        result.tcpEstablished_ = tcpEstablished_;
        if (((from_bitField0_ & 0x00000020) == 0x00000020)) {
          to_bitField0_ |= 0x00000020;
        }
        result.evasiveDevices_ = evasiveDevices_;
        if (((from_bitField0_ & 0x00000040) == 0x00000040)) {
          to_bitField0_ |= 0x00000040;
        }
        result.shellJob_ = shellJob_;
        if (((from_bitField0_ & 0x00000080) == 0x00000080)) {
          to_bitField0_ |= 0x00000080;
        }
        result.fileLocks_ = fileLocks_;
        if (((from_bitField0_ & 0x00000100) == 0x00000100)) {
          to_bitField0_ |= 0x00000100;
        }
        result.logLevel_ = logLevel_;
        if (((from_bitField0_ & 0x00000200) == 0x00000200)) {
          to_bitField0_ |= 0x00000200;
        }
        result.logFile_ = logFile_;
        if (((from_bitField0_ & 0x00000400) == 0x00000400)) {
          to_bitField0_ |= 0x00000400;
        }
        if (psBuilder_ == null) {
          result.ps_ = ps_;
        } else {
          result.ps_ = psBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000800) == 0x00000800)) {
          to_bitField0_ |= 0x00000800;
        }
        result.notifyScripts_ = notifyScripts_;
        if (((from_bitField0_ & 0x00001000) == 0x00001000)) {
          to_bitField0_ |= 0x00001000;
        }
        result.root_ = root_;
        if (((from_bitField0_ & 0x00002000) == 0x00002000)) {
          to_bitField0_ |= 0x00002000;
        }
        result.parentImg_ = parentImg_;
        if (((from_bitField0_ & 0x00004000) == 0x00004000)) {
          to_bitField0_ |= 0x00004000;
        }
        result.trackMem_ = trackMem_;
        if (((from_bitField0_ & 0x00008000) == 0x00008000)) {
          to_bitField0_ |= 0x00008000;
        }
        result.autoDedup_ = autoDedup_;
        if (((from_bitField0_ & 0x00010000) == 0x00010000)) {
          to_bitField0_ |= 0x00010000;
        }
        result.workDirFd_ = workDirFd_;
        if (((from_bitField0_ & 0x00020000) == 0x00020000)) {
          to_bitField0_ |= 0x00020000;
        }
        result.linkRemap_ = linkRemap_;
        if (vethsBuilder_ == null) {
          if (((bitField0_ & 0x00040000) == 0x00040000)) {
            veths_ = java.util.Collections.unmodifiableList(veths_);
            bitField0_ = (bitField0_ & ~0x00040000);
          }
          result.veths_ = veths_;
        } else {
          result.veths_ = vethsBuilder_.build();
        }
        if (((from_bitField0_ & 0x00080000) == 0x00080000)) {
          to_bitField0_ |= 0x00040000;
        }
        result.cpuCap_ = cpuCap_;
        if (((from_bitField0_ & 0x00100000) == 0x00100000)) {
          to_bitField0_ |= 0x00080000;
        }
        result.forceIrmap_ = forceIrmap_;
        if (((bitField0_ & 0x00200000) == 0x00200000)) {
          execCmd_ = execCmd_.getUnmodifiableView();
          bitField0_ = (bitField0_ & ~0x00200000);
        }
        result.execCmd_ = execCmd_;
        if (extMntBuilder_ == null) {
          if (((bitField0_ & 0x00400000) == 0x00400000)) {
            extMnt_ = java.util.Collections.unmodifiableList(extMnt_);
            bitField0_ = (bitField0_ & ~0x00400000);
          }
          result.extMnt_ = extMnt_;
        } else {
          result.extMnt_ = extMntBuilder_.build();
        }
        if (((from_bitField0_ & 0x00800000) == 0x00800000)) {
          to_bitField0_ |= 0x00100000;
        }
        result.manageCgroups_ = manageCgroups_;
        if (cgRootBuilder_ == null) {
          if (((bitField0_ & 0x01000000) == 0x01000000)) {
            cgRoot_ = java.util.Collections.unmodifiableList(cgRoot_);
            bitField0_ = (bitField0_ & ~0x01000000);
          }
          result.cgRoot_ = cgRoot_;
        } else {
          result.cgRoot_ = cgRootBuilder_.build();
        }
        if (((from_bitField0_ & 0x02000000) == 0x02000000)) {
          to_bitField0_ |= 0x00200000;
        }
        result.rstSibling_ = rstSibling_;
        if (((from_bitField0_ & 0x04000000) == 0x04000000)) {
          to_bitField0_ |= 0x00400000;
        }
        result.appNum_ = appNum_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CriuRPC.criu_opts) {
          return mergeFrom((CriuRPC.criu_opts) other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CriuRPC.criu_opts other) {
        if (other == CriuRPC.criu_opts.getDefaultInstance()) return this;
        if (other.hasImagesDirFd()) {
          setImagesDirFd(other.getImagesDirFd());
        }
        if (other.hasPid()) {
          setPid(other.getPid());
        }
        if (other.hasLeaveRunning()) {
          setLeaveRunning(other.getLeaveRunning());
        }
        if (other.hasExtUnixSk()) {
          setExtUnixSk(other.getExtUnixSk());
        }
        if (other.hasTcpEstablished()) {
          setTcpEstablished(other.getTcpEstablished());
        }
        if (other.hasEvasiveDevices()) {
          setEvasiveDevices(other.getEvasiveDevices());
        }
        if (other.hasShellJob()) {
          setShellJob(other.getShellJob());
        }
        if (other.hasFileLocks()) {
          setFileLocks(other.getFileLocks());
        }
        if (other.hasLogLevel()) {
          setLogLevel(other.getLogLevel());
        }
        if (other.hasLogFile()) {
          bitField0_ |= 0x00000200;
          logFile_ = other.logFile_;
          onChanged();
        }
        if (other.hasPs()) {
          mergePs(other.getPs());
        }
        if (other.hasNotifyScripts()) {
          setNotifyScripts(other.getNotifyScripts());
        }
        if (other.hasRoot()) {
          bitField0_ |= 0x00001000;
          root_ = other.root_;
          onChanged();
        }
        if (other.hasParentImg()) {
          bitField0_ |= 0x00002000;
          parentImg_ = other.parentImg_;
          onChanged();
        }
        if (other.hasTrackMem()) {
          setTrackMem(other.getTrackMem());
        }
        if (other.hasAutoDedup()) {
          setAutoDedup(other.getAutoDedup());
        }
        if (other.hasWorkDirFd()) {
          setWorkDirFd(other.getWorkDirFd());
        }
        if (other.hasLinkRemap()) {
          setLinkRemap(other.getLinkRemap());
        }
        if (vethsBuilder_ == null) {
          if (!other.veths_.isEmpty()) {
            if (veths_.isEmpty()) {
              veths_ = other.veths_;
              bitField0_ = (bitField0_ & ~0x00040000);
            } else {
              ensureVethsIsMutable();
              veths_.addAll(other.veths_);
            }
            onChanged();
          }
        } else {
          if (!other.veths_.isEmpty()) {
            if (vethsBuilder_.isEmpty()) {
              vethsBuilder_.dispose();
              vethsBuilder_ = null;
              veths_ = other.veths_;
              bitField0_ = (bitField0_ & ~0x00040000);
              vethsBuilder_ =
                      com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                              getVethsFieldBuilder() : null;
            } else {
              vethsBuilder_.addAllMessages(other.veths_);
            }
          }
        }
        if (other.hasCpuCap()) {
          setCpuCap(other.getCpuCap());
        }
        if (other.hasForceIrmap()) {
          setForceIrmap(other.getForceIrmap());
        }
        if (!other.execCmd_.isEmpty()) {
          if (execCmd_.isEmpty()) {
            execCmd_ = other.execCmd_;
            bitField0_ = (bitField0_ & ~0x00200000);
          } else {
            ensureExecCmdIsMutable();
            execCmd_.addAll(other.execCmd_);
          }
          onChanged();
        }
        if (extMntBuilder_ == null) {
          if (!other.extMnt_.isEmpty()) {
            if (extMnt_.isEmpty()) {
              extMnt_ = other.extMnt_;
              bitField0_ = (bitField0_ & ~0x00400000);
            } else {
              ensureExtMntIsMutable();
              extMnt_.addAll(other.extMnt_);
            }
            onChanged();
          }
        } else {
          if (!other.extMnt_.isEmpty()) {
            if (extMntBuilder_.isEmpty()) {
              extMntBuilder_.dispose();
              extMntBuilder_ = null;
              extMnt_ = other.extMnt_;
              bitField0_ = (bitField0_ & ~0x00400000);
              extMntBuilder_ =
                      com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                              getExtMntFieldBuilder() : null;
            } else {
              extMntBuilder_.addAllMessages(other.extMnt_);
            }
          }
        }
        if (other.hasManageCgroups()) {
          setManageCgroups(other.getManageCgroups());
        }
        if (cgRootBuilder_ == null) {
          if (!other.cgRoot_.isEmpty()) {
            if (cgRoot_.isEmpty()) {
              cgRoot_ = other.cgRoot_;
              bitField0_ = (bitField0_ & ~0x01000000);
            } else {
              ensureCgRootIsMutable();
              cgRoot_.addAll(other.cgRoot_);
            }
            onChanged();
          }
        } else {
          if (!other.cgRoot_.isEmpty()) {
            if (cgRootBuilder_.isEmpty()) {
              cgRootBuilder_.dispose();
              cgRootBuilder_ = null;
              cgRoot_ = other.cgRoot_;
              bitField0_ = (bitField0_ & ~0x01000000);
              cgRootBuilder_ =
                      com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                              getCgRootFieldBuilder() : null;
            } else {
              cgRootBuilder_.addAllMessages(other.cgRoot_);
            }
          }
        }
        if (other.hasRstSibling()) {
          setRstSibling(other.getRstSibling());
        }
        if (other.hasAppNum()) {
          setAppNum(other.getAppNum());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        if (!hasImagesDirFd()) {

          return false;
        }
        if (!hasAppNum()) {

          return false;
        }
        for (int i = 0; i < getVethsCount(); i++) {
          if (!getVeths(i).isInitialized()) {

            return false;
          }
        }
        for (int i = 0; i < getExtMntCount(); i++) {
          if (!getExtMnt(i).isInitialized()) {

            return false;
          }
        }
        for (int i = 0; i < getCgRootCount(); i++) {
          if (!getCgRoot(i).isInitialized()) {

            return false;
          }
        }
        return true;
      }

      public Builder mergeFrom(
              com.google.protobuf.CodedInputStream input,
              com.google.protobuf.ExtensionRegistryLite extensionRegistry)
              throws java.io.IOException {
        CriuRPC.criu_opts parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CriuRPC.criu_opts) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private int bitField0_;

      private int imagesDirFd_;

      /**
       * <code>required int32 images_dir_fd = 1;</code>
       */
      public boolean hasImagesDirFd() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }

      /**
       * <code>required int32 images_dir_fd = 1;</code>
       */
      public int getImagesDirFd() {
        return imagesDirFd_;
      }

      /**
       * <code>required int32 images_dir_fd = 1;</code>
       */
      public Builder setImagesDirFd(int value) {
        bitField0_ |= 0x00000001;
        imagesDirFd_ = value;
        onChanged();
        return this;
      }

      /**
       * <code>required int32 images_dir_fd = 1;</code>
       */
      public Builder clearImagesDirFd() {
        bitField0_ = (bitField0_ & ~0x00000001);
        imagesDirFd_ = 0;
        onChanged();
        return this;
      }

      private int pid_;

      /**
       * <code>optional int32 pid = 2;</code>
       *
       * <pre>
       * if not set on dump, will dump requesting process 
       * </pre>
       */
      public boolean hasPid() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }

      /**
       * <code>optional int32 pid = 2;</code>
       *
       * <pre>
       * if not set on dump, will dump requesting process 
       * </pre>
       */
      public int getPid() {
        return pid_;
      }

      /**
       * <code>optional int32 pid = 2;</code>
       *
       * <pre>
       * if not set on dump, will dump requesting process 
       * </pre>
       */
      public Builder setPid(int value) {
        bitField0_ |= 0x00000002;
        pid_ = value;
        onChanged();
        return this;
      }

      /**
       * <code>optional int32 pid = 2;</code>
       *
       * <pre>
       * if not set on dump, will dump requesting process 
       * </pre>
       */
      public Builder clearPid() {
        bitField0_ = (bitField0_ & ~0x00000002);
        pid_ = 0;
        onChanged();
        return this;
      }

      private boolean leaveRunning_;

      /**
       * <code>optional bool leave_running = 3;</code>
       */
      public boolean hasLeaveRunning() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }

      /**
       * <code>optional bool leave_running = 3;</code>
       */
      public boolean getLeaveRunning() {
        return leaveRunning_;
      }

      /**
       * <code>optional bool leave_running = 3;</code>
       */
      public Builder setLeaveRunning(boolean value) {
        bitField0_ |= 0x00000004;
        leaveRunning_ = value;
        onChanged();
        return this;
      }

      /**
       * <code>optional bool leave_running = 3;</code>
       */
      public Builder clearLeaveRunning() {
        bitField0_ = (bitField0_ & ~0x00000004);
        leaveRunning_ = false;
        onChanged();
        return this;
      }

      private boolean extUnixSk_;

      /**
       * <code>optional bool ext_unix_sk = 4;</code>
       */
      public boolean hasExtUnixSk() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }

      /**
       * <code>optional bool ext_unix_sk = 4;</code>
       */
      public boolean getExtUnixSk() {
        return extUnixSk_;
      }

      /**
       * <code>optional bool ext_unix_sk = 4;</code>
       */
      public Builder setExtUnixSk(boolean value) {
        bitField0_ |= 0x00000008;
        extUnixSk_ = value;
        onChanged();
        return this;
      }

      /**
       * <code>optional bool ext_unix_sk = 4;</code>
       */
      public Builder clearExtUnixSk() {
        bitField0_ = (bitField0_ & ~0x00000008);
        extUnixSk_ = false;
        onChanged();
        return this;
      }

      private boolean tcpEstablished_;

      /**
       * <code>optional bool tcp_established = 5;</code>
       */
      public boolean hasTcpEstablished() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }

      /**
       * <code>optional bool tcp_established = 5;</code>
       */
      public boolean getTcpEstablished() {
        return tcpEstablished_;
      }

      /**
       * <code>optional bool tcp_established = 5;</code>
       */
      public Builder setTcpEstablished(boolean value) {
        bitField0_ |= 0x00000010;
        tcpEstablished_ = value;
        onChanged();
        return this;
      }

      /**
       * <code>optional bool tcp_established = 5;</code>
       */
      public Builder clearTcpEstablished() {
        bitField0_ = (bitField0_ & ~0x00000010);
        tcpEstablished_ = false;
        onChanged();
        return this;
      }

      private boolean evasiveDevices_;

      /**
       * <code>optional bool evasive_devices = 6;</code>
       */
      public boolean hasEvasiveDevices() {
        return ((bitField0_ & 0x00000020) == 0x00000020);
      }

      /**
       * <code>optional bool evasive_devices = 6;</code>
       */
      public boolean getEvasiveDevices() {
        return evasiveDevices_;
      }

      /**
       * <code>optional bool evasive_devices = 6;</code>
       */
      public Builder setEvasiveDevices(boolean value) {
        bitField0_ |= 0x00000020;
        evasiveDevices_ = value;
        onChanged();
        return this;
      }

      /**
       * <code>optional bool evasive_devices = 6;</code>
       */
      public Builder clearEvasiveDevices() {
        bitField0_ = (bitField0_ & ~0x00000020);
        evasiveDevices_ = false;
        onChanged();
        return this;
      }

      private boolean shellJob_;

      /**
       * <code>optional bool shell_job = 7;</code>
       */
      public boolean hasShellJob() {
        return ((bitField0_ & 0x00000040) == 0x00000040);
      }

      /**
       * <code>optional bool shell_job = 7;</code>
       */
      public boolean getShellJob() {
        return shellJob_;
      }

      /**
       * <code>optional bool shell_job = 7;</code>
       */
      public Builder setShellJob(boolean value) {
        bitField0_ |= 0x00000040;
        shellJob_ = value;
        onChanged();
        return this;
      }

      /**
       * <code>optional bool shell_job = 7;</code>
       */
      public Builder clearShellJob() {
        bitField0_ = (bitField0_ & ~0x00000040);
        shellJob_ = false;
        onChanged();
        return this;
      }

      private boolean fileLocks_;

      /**
       * <code>optional bool file_locks = 8;</code>
       */
      public boolean hasFileLocks() {
        return ((bitField0_ & 0x00000080) == 0x00000080);
      }

      /**
       * <code>optional bool file_locks = 8;</code>
       */
      public boolean getFileLocks() {
        return fileLocks_;
      }

      /**
       * <code>optional bool file_locks = 8;</code>
       */
      public Builder setFileLocks(boolean value) {
        bitField0_ |= 0x00000080;
        fileLocks_ = value;
        onChanged();
        return this;
      }

      /**
       * <code>optional bool file_locks = 8;</code>
       */
      public Builder clearFileLocks() {
        bitField0_ = (bitField0_ & ~0x00000080);
        fileLocks_ = false;
        onChanged();
        return this;
      }

      private int logLevel_ = 2;

      /**
       * <code>optional int32 log_level = 9 [default = 2];</code>
       */
      public boolean hasLogLevel() {
        return ((bitField0_ & 0x00000100) == 0x00000100);
      }

      /**
       * <code>optional int32 log_level = 9 [default = 2];</code>
       */
      public int getLogLevel() {
        return logLevel_;
      }

      /**
       * <code>optional int32 log_level = 9 [default = 2];</code>
       */
      public Builder setLogLevel(int value) {
        bitField0_ |= 0x00000100;
        logLevel_ = value;
        onChanged();
        return this;
      }

      /**
       * <code>optional int32 log_level = 9 [default = 2];</code>
       */
      public Builder clearLogLevel() {
        bitField0_ = (bitField0_ & ~0x00000100);
        logLevel_ = 2;
        onChanged();
        return this;
      }

      private java.lang.Object logFile_ = "";

      /**
       * <code>optional string log_file = 10;</code>
       *
       * <pre>
       * No subdirs are allowed. Consider using work-dir 
       * </pre>
       */
      public boolean hasLogFile() {
        return ((bitField0_ & 0x00000200) == 0x00000200);
      }

      /**
       * <code>optional string log_file = 10;</code>
       *
       * <pre>
       * No subdirs are allowed. Consider using work-dir 
       * </pre>
       */
      public java.lang.String getLogFile() {
        java.lang.Object ref = logFile_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
                  (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            logFile_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }

      /**
       * <code>optional string log_file = 10;</code>
       *
       * <pre>
       * No subdirs are allowed. Consider using work-dir 
       * </pre>
       */
      public com.google.protobuf.ByteString
      getLogFileBytes() {
        java.lang.Object ref = logFile_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b =
                  com.google.protobuf.ByteString.copyFromUtf8(
                          (java.lang.String) ref);
          logFile_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      /**
       * <code>optional string log_file = 10;</code>
       *
       * <pre>
       * No subdirs are allowed. Consider using work-dir 
       * </pre>
       */
      public Builder setLogFile(
              java.lang.String value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000200;
        logFile_ = value;
        onChanged();
        return this;
      }

      /**
       * <code>optional string log_file = 10;</code>
       *
       * <pre>
       * No subdirs are allowed. Consider using work-dir 
       * </pre>
       */
      public Builder clearLogFile() {
        bitField0_ = (bitField0_ & ~0x00000200);
        logFile_ = getDefaultInstance().getLogFile();
        onChanged();
        return this;
      }

      /**
       * <code>optional string log_file = 10;</code>
       *
       * <pre>
       * No subdirs are allowed. Consider using work-dir 
       * </pre>
       */
      public Builder setLogFileBytes(
              com.google.protobuf.ByteString value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000200;
        logFile_ = value;
        onChanged();
        return this;
      }

      private CriuRPC.criu_page_server_info ps_ = CriuRPC.criu_page_server_info.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
              CriuRPC.criu_page_server_info, CriuRPC.criu_page_server_info.Builder, CriuRPC
              .criu_page_server_infoOrBuilder>
              psBuilder_;

      /**
       * <code>optional .criu_page_server_info ps = 11;</code>
       */
      public boolean hasPs() {
        return ((bitField0_ & 0x00000400) == 0x00000400);
      }

      /**
       * <code>optional .criu_page_server_info ps = 11;</code>
       */
      public CriuRPC.criu_page_server_info getPs() {
        if (psBuilder_ == null) {
          return ps_;
        } else {
          return psBuilder_.getMessage();
        }
      }

      /**
       * <code>optional .criu_page_server_info ps = 11;</code>
       */
      public Builder setPs(CriuRPC.criu_page_server_info value) {
        if (psBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ps_ = value;
          onChanged();
        } else {
          psBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000400;
        return this;
      }

      /**
       * <code>optional .criu_page_server_info ps = 11;</code>
       */
      public Builder setPs(
              CriuRPC.criu_page_server_info.Builder builderForValue) {
        if (psBuilder_ == null) {
          ps_ = builderForValue.build();
          onChanged();
        } else {
          psBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000400;
        return this;
      }

      /**
       * <code>optional .criu_page_server_info ps = 11;</code>
       */
      public Builder mergePs(CriuRPC.criu_page_server_info value) {
        if (psBuilder_ == null) {
          if (((bitField0_ & 0x00000400) == 0x00000400) &&
              ps_ != CriuRPC.criu_page_server_info.getDefaultInstance()) {
            ps_ =
                    CriuRPC.criu_page_server_info.newBuilder(ps_).mergeFrom(value).buildPartial();
          } else {
            ps_ = value;
          }
          onChanged();
        } else {
          psBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000400;
        return this;
      }

      /**
       * <code>optional .criu_page_server_info ps = 11;</code>
       */
      public Builder clearPs() {
        if (psBuilder_ == null) {
          ps_ = CriuRPC.criu_page_server_info.getDefaultInstance();
          onChanged();
        } else {
          psBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000400);
        return this;
      }

      /**
       * <code>optional .criu_page_server_info ps = 11;</code>
       */
      public CriuRPC.criu_page_server_info.Builder getPsBuilder() {
        bitField0_ |= 0x00000400;
        onChanged();
        return getPsFieldBuilder().getBuilder();
      }

      /**
       * <code>optional .criu_page_server_info ps = 11;</code>
       */
      public CriuRPC.criu_page_server_infoOrBuilder getPsOrBuilder() {
        if (psBuilder_ != null) {
          return psBuilder_.getMessageOrBuilder();
        } else {
          return ps_;
        }
      }

      /**
       * <code>optional .criu_page_server_info ps = 11;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
              CriuRPC.criu_page_server_info, CriuRPC.criu_page_server_info.Builder, CriuRPC
              .criu_page_server_infoOrBuilder>
      getPsFieldBuilder() {
        if (psBuilder_ == null) {
          psBuilder_ = new com.google.protobuf.SingleFieldBuilder<
                  CriuRPC.criu_page_server_info, CriuRPC.criu_page_server_info.Builder, CriuRPC
                  .criu_page_server_infoOrBuilder>(
                  getPs(),
                  getParentForChildren(),
                  isClean());
          ps_ = null;
        }
        return psBuilder_;
      }

      private boolean notifyScripts_;

      /**
       * <code>optional bool notify_scripts = 12;</code>
       */
      public boolean hasNotifyScripts() {
        return ((bitField0_ & 0x00000800) == 0x00000800);
      }

      /**
       * <code>optional bool notify_scripts = 12;</code>
       */
      public boolean getNotifyScripts() {
        return notifyScripts_;
      }

      /**
       * <code>optional bool notify_scripts = 12;</code>
       */
      public Builder setNotifyScripts(boolean value) {
        bitField0_ |= 0x00000800;
        notifyScripts_ = value;
        onChanged();
        return this;
      }

      /**
       * <code>optional bool notify_scripts = 12;</code>
       */
      public Builder clearNotifyScripts() {
        bitField0_ = (bitField0_ & ~0x00000800);
        notifyScripts_ = false;
        onChanged();
        return this;
      }

      private java.lang.Object root_ = "";

      /**
       * <code>optional string root = 13;</code>
       */
      public boolean hasRoot() {
        return ((bitField0_ & 0x00001000) == 0x00001000);
      }

      /**
       * <code>optional string root = 13;</code>
       */
      public java.lang.String getRoot() {
        java.lang.Object ref = root_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
                  (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            root_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }

      /**
       * <code>optional string root = 13;</code>
       */
      public com.google.protobuf.ByteString
      getRootBytes() {
        java.lang.Object ref = root_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b =
                  com.google.protobuf.ByteString.copyFromUtf8(
                          (java.lang.String) ref);
          root_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      /**
       * <code>optional string root = 13;</code>
       */
      public Builder setRoot(
              java.lang.String value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00001000;
        root_ = value;
        onChanged();
        return this;
      }

      /**
       * <code>optional string root = 13;</code>
       */
      public Builder clearRoot() {
        bitField0_ = (bitField0_ & ~0x00001000);
        root_ = getDefaultInstance().getRoot();
        onChanged();
        return this;
      }

      /**
       * <code>optional string root = 13;</code>
       */
      public Builder setRootBytes(
              com.google.protobuf.ByteString value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00001000;
        root_ = value;
        onChanged();
        return this;
      }

      private java.lang.Object parentImg_ = "";

      /**
       * <code>optional string parent_img = 14;</code>
       */
      public boolean hasParentImg() {
        return ((bitField0_ & 0x00002000) == 0x00002000);
      }

      /**
       * <code>optional string parent_img = 14;</code>
       */
      public java.lang.String getParentImg() {
        java.lang.Object ref = parentImg_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
                  (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            parentImg_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }

      /**
       * <code>optional string parent_img = 14;</code>
       */
      public com.google.protobuf.ByteString
      getParentImgBytes() {
        java.lang.Object ref = parentImg_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b =
                  com.google.protobuf.ByteString.copyFromUtf8(
                          (java.lang.String) ref);
          parentImg_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      /**
       * <code>optional string parent_img = 14;</code>
       */
      public Builder setParentImg(
              java.lang.String value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00002000;
        parentImg_ = value;
        onChanged();
        return this;
      }

      /**
       * <code>optional string parent_img = 14;</code>
       */
      public Builder clearParentImg() {
        bitField0_ = (bitField0_ & ~0x00002000);
        parentImg_ = getDefaultInstance().getParentImg();
        onChanged();
        return this;
      }

      /**
       * <code>optional string parent_img = 14;</code>
       */
      public Builder setParentImgBytes(
              com.google.protobuf.ByteString value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00002000;
        parentImg_ = value;
        onChanged();
        return this;
      }

      private boolean trackMem_;

      /**
       * <code>optional bool track_mem = 15;</code>
       */
      public boolean hasTrackMem() {
        return ((bitField0_ & 0x00004000) == 0x00004000);
      }

      /**
       * <code>optional bool track_mem = 15;</code>
       */
      public boolean getTrackMem() {
        return trackMem_;
      }

      /**
       * <code>optional bool track_mem = 15;</code>
       */
      public Builder setTrackMem(boolean value) {
        bitField0_ |= 0x00004000;
        trackMem_ = value;
        onChanged();
        return this;
      }

      /**
       * <code>optional bool track_mem = 15;</code>
       */
      public Builder clearTrackMem() {
        bitField0_ = (bitField0_ & ~0x00004000);
        trackMem_ = false;
        onChanged();
        return this;
      }

      private boolean autoDedup_;

      /**
       * <code>optional bool auto_dedup = 16;</code>
       */
      public boolean hasAutoDedup() {
        return ((bitField0_ & 0x00008000) == 0x00008000);
      }

      /**
       * <code>optional bool auto_dedup = 16;</code>
       */
      public boolean getAutoDedup() {
        return autoDedup_;
      }

      /**
       * <code>optional bool auto_dedup = 16;</code>
       */
      public Builder setAutoDedup(boolean value) {
        bitField0_ |= 0x00008000;
        autoDedup_ = value;
        onChanged();
        return this;
      }

      /**
       * <code>optional bool auto_dedup = 16;</code>
       */
      public Builder clearAutoDedup() {
        bitField0_ = (bitField0_ & ~0x00008000);
        autoDedup_ = false;
        onChanged();
        return this;
      }

      private int workDirFd_;

      /**
       * <code>optional int32 work_dir_fd = 17;</code>
       */
      public boolean hasWorkDirFd() {
        return ((bitField0_ & 0x00010000) == 0x00010000);
      }

      /**
       * <code>optional int32 work_dir_fd = 17;</code>
       */
      public int getWorkDirFd() {
        return workDirFd_;
      }

      /**
       * <code>optional int32 work_dir_fd = 17;</code>
       */
      public Builder setWorkDirFd(int value) {
        bitField0_ |= 0x00010000;
        workDirFd_ = value;
        onChanged();
        return this;
      }

      /**
       * <code>optional int32 work_dir_fd = 17;</code>
       */
      public Builder clearWorkDirFd() {
        bitField0_ = (bitField0_ & ~0x00010000);
        workDirFd_ = 0;
        onChanged();
        return this;
      }

      private boolean linkRemap_;

      /**
       * <code>optional bool link_remap = 18;</code>
       */
      public boolean hasLinkRemap() {
        return ((bitField0_ & 0x00020000) == 0x00020000);
      }

      /**
       * <code>optional bool link_remap = 18;</code>
       */
      public boolean getLinkRemap() {
        return linkRemap_;
      }

      /**
       * <code>optional bool link_remap = 18;</code>
       */
      public Builder setLinkRemap(boolean value) {
        bitField0_ |= 0x00020000;
        linkRemap_ = value;
        onChanged();
        return this;
      }

      /**
       * <code>optional bool link_remap = 18;</code>
       */
      public Builder clearLinkRemap() {
        bitField0_ = (bitField0_ & ~0x00020000);
        linkRemap_ = false;
        onChanged();
        return this;
      }

      private java.util.List<CriuRPC.criu_veth_pair> veths_ =
              java.util.Collections.emptyList();

      private void ensureVethsIsMutable() {
        if (!((bitField0_ & 0x00040000) == 0x00040000)) {
          veths_ = new java.util.ArrayList<CriuRPC.criu_veth_pair>(veths_);
          bitField0_ |= 0x00040000;
        }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
              CriuRPC.criu_veth_pair, CriuRPC.criu_veth_pair.Builder, CriuRPC.criu_veth_pairOrBuilder> vethsBuilder_;

      /**
       * <code>repeated .criu_veth_pair veths = 19;</code>
       */
      public java.util.List<CriuRPC.criu_veth_pair> getVethsList() {
        if (vethsBuilder_ == null) {
          return java.util.Collections.unmodifiableList(veths_);
        } else {
          return vethsBuilder_.getMessageList();
        }
      }

      /**
       * <code>repeated .criu_veth_pair veths = 19;</code>
       */
      public int getVethsCount() {
        if (vethsBuilder_ == null) {
          return veths_.size();
        } else {
          return vethsBuilder_.getCount();
        }
      }

      /**
       * <code>repeated .criu_veth_pair veths = 19;</code>
       */
      public CriuRPC.criu_veth_pair getVeths(int index) {
        if (vethsBuilder_ == null) {
          return veths_.get(index);
        } else {
          return vethsBuilder_.getMessage(index);
        }
      }

      /**
       * <code>repeated .criu_veth_pair veths = 19;</code>
       */
      public Builder setVeths(
              int index, CriuRPC.criu_veth_pair value) {
        if (vethsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureVethsIsMutable();
          veths_.set(index, value);
          onChanged();
        } else {
          vethsBuilder_.setMessage(index, value);
        }
        return this;
      }

      /**
       * <code>repeated .criu_veth_pair veths = 19;</code>
       */
      public Builder setVeths(
              int index, CriuRPC.criu_veth_pair.Builder builderForValue) {
        if (vethsBuilder_ == null) {
          ensureVethsIsMutable();
          veths_.set(index, builderForValue.build());
          onChanged();
        } else {
          vethsBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }

      /**
       * <code>repeated .criu_veth_pair veths = 19;</code>
       */
      public Builder addVeths(CriuRPC.criu_veth_pair value) {
        if (vethsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureVethsIsMutable();
          veths_.add(value);
          onChanged();
        } else {
          vethsBuilder_.addMessage(value);
        }
        return this;
      }

      /**
       * <code>repeated .criu_veth_pair veths = 19;</code>
       */
      public Builder addVeths(
              int index, CriuRPC.criu_veth_pair value) {
        if (vethsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureVethsIsMutable();
          veths_.add(index, value);
          onChanged();
        } else {
          vethsBuilder_.addMessage(index, value);
        }
        return this;
      }

      /**
       * <code>repeated .criu_veth_pair veths = 19;</code>
       */
      public Builder addVeths(
              CriuRPC.criu_veth_pair.Builder builderForValue) {
        if (vethsBuilder_ == null) {
          ensureVethsIsMutable();
          veths_.add(builderForValue.build());
          onChanged();
        } else {
          vethsBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }

      /**
       * <code>repeated .criu_veth_pair veths = 19;</code>
       */
      public Builder addVeths(
              int index, CriuRPC.criu_veth_pair.Builder builderForValue) {
        if (vethsBuilder_ == null) {
          ensureVethsIsMutable();
          veths_.add(index, builderForValue.build());
          onChanged();
        } else {
          vethsBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }

      /**
       * <code>repeated .criu_veth_pair veths = 19;</code>
       */
      public Builder addAllVeths(
              java.lang.Iterable<? extends CriuRPC.criu_veth_pair> values) {
        if (vethsBuilder_ == null) {
          ensureVethsIsMutable();
          com.google.protobuf.AbstractMessageLite.Builder.addAll(
                  values, veths_);
          onChanged();
        } else {
          vethsBuilder_.addAllMessages(values);
        }
        return this;
      }

      /**
       * <code>repeated .criu_veth_pair veths = 19;</code>
       */
      public Builder clearVeths() {
        if (vethsBuilder_ == null) {
          veths_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00040000);
          onChanged();
        } else {
          vethsBuilder_.clear();
        }
        return this;
      }

      /**
       * <code>repeated .criu_veth_pair veths = 19;</code>
       */
      public Builder removeVeths(int index) {
        if (vethsBuilder_ == null) {
          ensureVethsIsMutable();
          veths_.remove(index);
          onChanged();
        } else {
          vethsBuilder_.remove(index);
        }
        return this;
      }

      /**
       * <code>repeated .criu_veth_pair veths = 19;</code>
       */
      public CriuRPC.criu_veth_pair.Builder getVethsBuilder(
              int index) {
        return getVethsFieldBuilder().getBuilder(index);
      }

      /**
       * <code>repeated .criu_veth_pair veths = 19;</code>
       */
      public CriuRPC.criu_veth_pairOrBuilder getVethsOrBuilder(
              int index) {
        if (vethsBuilder_ == null) {
          return veths_.get(index);
        } else {
          return vethsBuilder_.getMessageOrBuilder(index);
        }
      }

      /**
       * <code>repeated .criu_veth_pair veths = 19;</code>
       */
      public java.util.List<? extends CriuRPC.criu_veth_pairOrBuilder>
      getVethsOrBuilderList() {
        if (vethsBuilder_ != null) {
          return vethsBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(veths_);
        }
      }

      /**
       * <code>repeated .criu_veth_pair veths = 19;</code>
       */
      public CriuRPC.criu_veth_pair.Builder addVethsBuilder() {
        return getVethsFieldBuilder().addBuilder(
                CriuRPC.criu_veth_pair.getDefaultInstance());
      }

      /**
       * <code>repeated .criu_veth_pair veths = 19;</code>
       */
      public CriuRPC.criu_veth_pair.Builder addVethsBuilder(
              int index) {
        return getVethsFieldBuilder().addBuilder(
                index, CriuRPC.criu_veth_pair.getDefaultInstance());
      }

      /**
       * <code>repeated .criu_veth_pair veths = 19;</code>
       */
      public java.util.List<CriuRPC.criu_veth_pair.Builder>
      getVethsBuilderList() {
        return getVethsFieldBuilder().getBuilderList();
      }

      private com.google.protobuf.RepeatedFieldBuilder<
              CriuRPC.criu_veth_pair, CriuRPC.criu_veth_pair.Builder, CriuRPC.criu_veth_pairOrBuilder>
      getVethsFieldBuilder() {
        if (vethsBuilder_ == null) {
          vethsBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
                  CriuRPC.criu_veth_pair, CriuRPC.criu_veth_pair.Builder, CriuRPC.criu_veth_pairOrBuilder>(
                  veths_,
                  ((bitField0_ & 0x00040000) == 0x00040000),
                  getParentForChildren(),
                  isClean());
          veths_ = null;
        }
        return vethsBuilder_;
      }

      private int cpuCap_ = -1;

      /**
       * <code>optional uint32 cpu_cap = 20 [default = 4294967295];</code>
       */
      public boolean hasCpuCap() {
        return ((bitField0_ & 0x00080000) == 0x00080000);
      }

      /**
       * <code>optional uint32 cpu_cap = 20 [default = 4294967295];</code>
       */
      public int getCpuCap() {
        return cpuCap_;
      }

      /**
       * <code>optional uint32 cpu_cap = 20 [default = 4294967295];</code>
       */
      public Builder setCpuCap(int value) {
        bitField0_ |= 0x00080000;
        cpuCap_ = value;
        onChanged();
        return this;
      }

      /**
       * <code>optional uint32 cpu_cap = 20 [default = 4294967295];</code>
       */
      public Builder clearCpuCap() {
        bitField0_ = (bitField0_ & ~0x00080000);
        cpuCap_ = -1;
        onChanged();
        return this;
      }

      private boolean forceIrmap_;

      /**
       * <code>optional bool force_irmap = 21;</code>
       */
      public boolean hasForceIrmap() {
        return ((bitField0_ & 0x00100000) == 0x00100000);
      }

      /**
       * <code>optional bool force_irmap = 21;</code>
       */
      public boolean getForceIrmap() {
        return forceIrmap_;
      }

      /**
       * <code>optional bool force_irmap = 21;</code>
       */
      public Builder setForceIrmap(boolean value) {
        bitField0_ |= 0x00100000;
        forceIrmap_ = value;
        onChanged();
        return this;
      }

      /**
       * <code>optional bool force_irmap = 21;</code>
       */
      public Builder clearForceIrmap() {
        bitField0_ = (bitField0_ & ~0x00100000);
        forceIrmap_ = false;
        onChanged();
        return this;
      }

      private com.google.protobuf.LazyStringList execCmd_ = com.google.protobuf.LazyStringArrayList.EMPTY;

      private void ensureExecCmdIsMutable() {
        if (!((bitField0_ & 0x00200000) == 0x00200000)) {
          execCmd_ = new com.google.protobuf.LazyStringArrayList(execCmd_);
          bitField0_ |= 0x00200000;
        }
      }

      /**
       * <code>repeated string exec_cmd = 22;</code>
       */
      public com.google.protobuf.ProtocolStringList
      getExecCmdList() {
        return execCmd_.getUnmodifiableView();
      }

      /**
       * <code>repeated string exec_cmd = 22;</code>
       */
      public int getExecCmdCount() {
        return execCmd_.size();
      }

      /**
       * <code>repeated string exec_cmd = 22;</code>
       */
      public java.lang.String getExecCmd(int index) {
        return execCmd_.get(index);
      }

      /**
       * <code>repeated string exec_cmd = 22;</code>
       */
      public com.google.protobuf.ByteString
      getExecCmdBytes(int index) {
        return execCmd_.getByteString(index);
      }

      /**
       * <code>repeated string exec_cmd = 22;</code>
       */
      public Builder setExecCmd(
              int index, java.lang.String value) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureExecCmdIsMutable();
        execCmd_.set(index, value);
        onChanged();
        return this;
      }

      /**
       * <code>repeated string exec_cmd = 22;</code>
       */
      public Builder addExecCmd(
              java.lang.String value) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureExecCmdIsMutable();
        execCmd_.add(value);
        onChanged();
        return this;
      }

      /**
       * <code>repeated string exec_cmd = 22;</code>
       */
      public Builder addAllExecCmd(
              java.lang.Iterable<java.lang.String> values) {
        ensureExecCmdIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
                values, execCmd_);
        onChanged();
        return this;
      }

      /**
       * <code>repeated string exec_cmd = 22;</code>
       */
      public Builder clearExecCmd() {
        execCmd_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00200000);
        onChanged();
        return this;
      }

      /**
       * <code>repeated string exec_cmd = 22;</code>
       */
      public Builder addExecCmdBytes(
              com.google.protobuf.ByteString value) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureExecCmdIsMutable();
        execCmd_.add(value);
        onChanged();
        return this;
      }

      private java.util.List<CriuRPC.ext_mount_map> extMnt_ =
              java.util.Collections.emptyList();

      private void ensureExtMntIsMutable() {
        if (!((bitField0_ & 0x00400000) == 0x00400000)) {
          extMnt_ = new java.util.ArrayList<CriuRPC.ext_mount_map>(extMnt_);
          bitField0_ |= 0x00400000;
        }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
              CriuRPC.ext_mount_map, CriuRPC.ext_mount_map.Builder, CriuRPC.ext_mount_mapOrBuilder> extMntBuilder_;

      /**
       * <code>repeated .ext_mount_map ext_mnt = 23;</code>
       */
      public java.util.List<CriuRPC.ext_mount_map> getExtMntList() {
        if (extMntBuilder_ == null) {
          return java.util.Collections.unmodifiableList(extMnt_);
        } else {
          return extMntBuilder_.getMessageList();
        }
      }

      /**
       * <code>repeated .ext_mount_map ext_mnt = 23;</code>
       */
      public int getExtMntCount() {
        if (extMntBuilder_ == null) {
          return extMnt_.size();
        } else {
          return extMntBuilder_.getCount();
        }
      }

      /**
       * <code>repeated .ext_mount_map ext_mnt = 23;</code>
       */
      public CriuRPC.ext_mount_map getExtMnt(int index) {
        if (extMntBuilder_ == null) {
          return extMnt_.get(index);
        } else {
          return extMntBuilder_.getMessage(index);
        }
      }

      /**
       * <code>repeated .ext_mount_map ext_mnt = 23;</code>
       */
      public Builder setExtMnt(
              int index, CriuRPC.ext_mount_map value) {
        if (extMntBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureExtMntIsMutable();
          extMnt_.set(index, value);
          onChanged();
        } else {
          extMntBuilder_.setMessage(index, value);
        }
        return this;
      }

      /**
       * <code>repeated .ext_mount_map ext_mnt = 23;</code>
       */
      public Builder setExtMnt(
              int index, CriuRPC.ext_mount_map.Builder builderForValue) {
        if (extMntBuilder_ == null) {
          ensureExtMntIsMutable();
          extMnt_.set(index, builderForValue.build());
          onChanged();
        } else {
          extMntBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }

      /**
       * <code>repeated .ext_mount_map ext_mnt = 23;</code>
       */
      public Builder addExtMnt(CriuRPC.ext_mount_map value) {
        if (extMntBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureExtMntIsMutable();
          extMnt_.add(value);
          onChanged();
        } else {
          extMntBuilder_.addMessage(value);
        }
        return this;
      }

      /**
       * <code>repeated .ext_mount_map ext_mnt = 23;</code>
       */
      public Builder addExtMnt(
              int index, CriuRPC.ext_mount_map value) {
        if (extMntBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureExtMntIsMutable();
          extMnt_.add(index, value);
          onChanged();
        } else {
          extMntBuilder_.addMessage(index, value);
        }
        return this;
      }

      /**
       * <code>repeated .ext_mount_map ext_mnt = 23;</code>
       */
      public Builder addExtMnt(
              CriuRPC.ext_mount_map.Builder builderForValue) {
        if (extMntBuilder_ == null) {
          ensureExtMntIsMutable();
          extMnt_.add(builderForValue.build());
          onChanged();
        } else {
          extMntBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }

      /**
       * <code>repeated .ext_mount_map ext_mnt = 23;</code>
       */
      public Builder addExtMnt(
              int index, CriuRPC.ext_mount_map.Builder builderForValue) {
        if (extMntBuilder_ == null) {
          ensureExtMntIsMutable();
          extMnt_.add(index, builderForValue.build());
          onChanged();
        } else {
          extMntBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }

      /**
       * <code>repeated .ext_mount_map ext_mnt = 23;</code>
       */
      public Builder addAllExtMnt(
              java.lang.Iterable<? extends CriuRPC.ext_mount_map> values) {
        if (extMntBuilder_ == null) {
          ensureExtMntIsMutable();
          com.google.protobuf.AbstractMessageLite.Builder.addAll(
                  values, extMnt_);
          onChanged();
        } else {
          extMntBuilder_.addAllMessages(values);
        }
        return this;
      }

      /**
       * <code>repeated .ext_mount_map ext_mnt = 23;</code>
       */
      public Builder clearExtMnt() {
        if (extMntBuilder_ == null) {
          extMnt_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00400000);
          onChanged();
        } else {
          extMntBuilder_.clear();
        }
        return this;
      }

      /**
       * <code>repeated .ext_mount_map ext_mnt = 23;</code>
       */
      public Builder removeExtMnt(int index) {
        if (extMntBuilder_ == null) {
          ensureExtMntIsMutable();
          extMnt_.remove(index);
          onChanged();
        } else {
          extMntBuilder_.remove(index);
        }
        return this;
      }

      /**
       * <code>repeated .ext_mount_map ext_mnt = 23;</code>
       */
      public CriuRPC.ext_mount_map.Builder getExtMntBuilder(
              int index) {
        return getExtMntFieldBuilder().getBuilder(index);
      }

      /**
       * <code>repeated .ext_mount_map ext_mnt = 23;</code>
       */
      public CriuRPC.ext_mount_mapOrBuilder getExtMntOrBuilder(
              int index) {
        if (extMntBuilder_ == null) {
          return extMnt_.get(index);
        } else {
          return extMntBuilder_.getMessageOrBuilder(index);
        }
      }

      /**
       * <code>repeated .ext_mount_map ext_mnt = 23;</code>
       */
      public java.util.List<? extends CriuRPC.ext_mount_mapOrBuilder>
      getExtMntOrBuilderList() {
        if (extMntBuilder_ != null) {
          return extMntBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(extMnt_);
        }
      }

      /**
       * <code>repeated .ext_mount_map ext_mnt = 23;</code>
       */
      public CriuRPC.ext_mount_map.Builder addExtMntBuilder() {
        return getExtMntFieldBuilder().addBuilder(
                CriuRPC.ext_mount_map.getDefaultInstance());
      }

      /**
       * <code>repeated .ext_mount_map ext_mnt = 23;</code>
       */
      public CriuRPC.ext_mount_map.Builder addExtMntBuilder(
              int index) {
        return getExtMntFieldBuilder().addBuilder(
                index, CriuRPC.ext_mount_map.getDefaultInstance());
      }

      /**
       * <code>repeated .ext_mount_map ext_mnt = 23;</code>
       */
      public java.util.List<CriuRPC.ext_mount_map.Builder>
      getExtMntBuilderList() {
        return getExtMntFieldBuilder().getBuilderList();
      }

      private com.google.protobuf.RepeatedFieldBuilder<
              CriuRPC.ext_mount_map, CriuRPC.ext_mount_map.Builder, CriuRPC.ext_mount_mapOrBuilder>
      getExtMntFieldBuilder() {
        if (extMntBuilder_ == null) {
          extMntBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
                  CriuRPC.ext_mount_map, CriuRPC.ext_mount_map.Builder, CriuRPC.ext_mount_mapOrBuilder>(
                  extMnt_,
                  ((bitField0_ & 0x00400000) == 0x00400000),
                  getParentForChildren(),
                  isClean());
          extMnt_ = null;
        }
        return extMntBuilder_;
      }

      private boolean manageCgroups_;

      /**
       * <code>optional bool manage_cgroups = 24;</code>
       */
      public boolean hasManageCgroups() {
        return ((bitField0_ & 0x00800000) == 0x00800000);
      }

      /**
       * <code>optional bool manage_cgroups = 24;</code>
       */
      public boolean getManageCgroups() {
        return manageCgroups_;
      }

      /**
       * <code>optional bool manage_cgroups = 24;</code>
       */
      public Builder setManageCgroups(boolean value) {
        bitField0_ |= 0x00800000;
        manageCgroups_ = value;
        onChanged();
        return this;
      }

      /**
       * <code>optional bool manage_cgroups = 24;</code>
       */
      public Builder clearManageCgroups() {
        bitField0_ = (bitField0_ & ~0x00800000);
        manageCgroups_ = false;
        onChanged();
        return this;
      }

      private java.util.List<CriuRPC.cgroup_root> cgRoot_ =
              java.util.Collections.emptyList();

      private void ensureCgRootIsMutable() {
        if (!((bitField0_ & 0x01000000) == 0x01000000)) {
          cgRoot_ = new java.util.ArrayList<CriuRPC.cgroup_root>(cgRoot_);
          bitField0_ |= 0x01000000;
        }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
              CriuRPC.cgroup_root, CriuRPC.cgroup_root.Builder, CriuRPC.cgroup_rootOrBuilder> cgRootBuilder_;

      /**
       * <code>repeated .cgroup_root cg_root = 25;</code>
       */
      public java.util.List<CriuRPC.cgroup_root> getCgRootList() {
        if (cgRootBuilder_ == null) {
          return java.util.Collections.unmodifiableList(cgRoot_);
        } else {
          return cgRootBuilder_.getMessageList();
        }
      }

      /**
       * <code>repeated .cgroup_root cg_root = 25;</code>
       */
      public int getCgRootCount() {
        if (cgRootBuilder_ == null) {
          return cgRoot_.size();
        } else {
          return cgRootBuilder_.getCount();
        }
      }

      /**
       * <code>repeated .cgroup_root cg_root = 25;</code>
       */
      public CriuRPC.cgroup_root getCgRoot(int index) {
        if (cgRootBuilder_ == null) {
          return cgRoot_.get(index);
        } else {
          return cgRootBuilder_.getMessage(index);
        }
      }

      /**
       * <code>repeated .cgroup_root cg_root = 25;</code>
       */
      public Builder setCgRoot(
              int index, CriuRPC.cgroup_root value) {
        if (cgRootBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureCgRootIsMutable();
          cgRoot_.set(index, value);
          onChanged();
        } else {
          cgRootBuilder_.setMessage(index, value);
        }
        return this;
      }

      /**
       * <code>repeated .cgroup_root cg_root = 25;</code>
       */
      public Builder setCgRoot(
              int index, CriuRPC.cgroup_root.Builder builderForValue) {
        if (cgRootBuilder_ == null) {
          ensureCgRootIsMutable();
          cgRoot_.set(index, builderForValue.build());
          onChanged();
        } else {
          cgRootBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }

      /**
       * <code>repeated .cgroup_root cg_root = 25;</code>
       */
      public Builder addCgRoot(CriuRPC.cgroup_root value) {
        if (cgRootBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureCgRootIsMutable();
          cgRoot_.add(value);
          onChanged();
        } else {
          cgRootBuilder_.addMessage(value);
        }
        return this;
      }

      /**
       * <code>repeated .cgroup_root cg_root = 25;</code>
       */
      public Builder addCgRoot(
              int index, CriuRPC.cgroup_root value) {
        if (cgRootBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureCgRootIsMutable();
          cgRoot_.add(index, value);
          onChanged();
        } else {
          cgRootBuilder_.addMessage(index, value);
        }
        return this;
      }

      /**
       * <code>repeated .cgroup_root cg_root = 25;</code>
       */
      public Builder addCgRoot(
              CriuRPC.cgroup_root.Builder builderForValue) {
        if (cgRootBuilder_ == null) {
          ensureCgRootIsMutable();
          cgRoot_.add(builderForValue.build());
          onChanged();
        } else {
          cgRootBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }

      /**
       * <code>repeated .cgroup_root cg_root = 25;</code>
       */
      public Builder addCgRoot(
              int index, CriuRPC.cgroup_root.Builder builderForValue) {
        if (cgRootBuilder_ == null) {
          ensureCgRootIsMutable();
          cgRoot_.add(index, builderForValue.build());
          onChanged();
        } else {
          cgRootBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }

      /**
       * <code>repeated .cgroup_root cg_root = 25;</code>
       */
      public Builder addAllCgRoot(
              java.lang.Iterable<? extends CriuRPC.cgroup_root> values) {
        if (cgRootBuilder_ == null) {
          ensureCgRootIsMutable();
          com.google.protobuf.AbstractMessageLite.Builder.addAll(
                  values, cgRoot_);
          onChanged();
        } else {
          cgRootBuilder_.addAllMessages(values);
        }
        return this;
      }

      /**
       * <code>repeated .cgroup_root cg_root = 25;</code>
       */
      public Builder clearCgRoot() {
        if (cgRootBuilder_ == null) {
          cgRoot_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x01000000);
          onChanged();
        } else {
          cgRootBuilder_.clear();
        }
        return this;
      }

      /**
       * <code>repeated .cgroup_root cg_root = 25;</code>
       */
      public Builder removeCgRoot(int index) {
        if (cgRootBuilder_ == null) {
          ensureCgRootIsMutable();
          cgRoot_.remove(index);
          onChanged();
        } else {
          cgRootBuilder_.remove(index);
        }
        return this;
      }

      /**
       * <code>repeated .cgroup_root cg_root = 25;</code>
       */
      public CriuRPC.cgroup_root.Builder getCgRootBuilder(
              int index) {
        return getCgRootFieldBuilder().getBuilder(index);
      }

      /**
       * <code>repeated .cgroup_root cg_root = 25;</code>
       */
      public CriuRPC.cgroup_rootOrBuilder getCgRootOrBuilder(
              int index) {
        if (cgRootBuilder_ == null) {
          return cgRoot_.get(index);
        } else {
          return cgRootBuilder_.getMessageOrBuilder(index);
        }
      }

      /**
       * <code>repeated .cgroup_root cg_root = 25;</code>
       */
      public java.util.List<? extends CriuRPC.cgroup_rootOrBuilder>
      getCgRootOrBuilderList() {
        if (cgRootBuilder_ != null) {
          return cgRootBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(cgRoot_);
        }
      }

      /**
       * <code>repeated .cgroup_root cg_root = 25;</code>
       */
      public CriuRPC.cgroup_root.Builder addCgRootBuilder() {
        return getCgRootFieldBuilder().addBuilder(
                CriuRPC.cgroup_root.getDefaultInstance());
      }

      /**
       * <code>repeated .cgroup_root cg_root = 25;</code>
       */
      public CriuRPC.cgroup_root.Builder addCgRootBuilder(
              int index) {
        return getCgRootFieldBuilder().addBuilder(
                index, CriuRPC.cgroup_root.getDefaultInstance());
      }

      /**
       * <code>repeated .cgroup_root cg_root = 25;</code>
       */
      public java.util.List<CriuRPC.cgroup_root.Builder>
      getCgRootBuilderList() {
        return getCgRootFieldBuilder().getBuilderList();
      }

      private com.google.protobuf.RepeatedFieldBuilder<
              CriuRPC.cgroup_root, CriuRPC.cgroup_root.Builder, CriuRPC.cgroup_rootOrBuilder>
      getCgRootFieldBuilder() {
        if (cgRootBuilder_ == null) {
          cgRootBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
                  CriuRPC.cgroup_root, CriuRPC.cgroup_root.Builder, CriuRPC.cgroup_rootOrBuilder>(
                  cgRoot_,
                  ((bitField0_ & 0x01000000) == 0x01000000),
                  getParentForChildren(),
                  isClean());
          cgRoot_ = null;
        }
        return cgRootBuilder_;
      }

      private boolean rstSibling_;

      /**
       * <code>optional bool rst_sibling = 26;</code>
       *
       * <pre>
       * swrk only 
       * </pre>
       */
      public boolean hasRstSibling() {
        return ((bitField0_ & 0x02000000) == 0x02000000);
      }

      /**
       * <code>optional bool rst_sibling = 26;</code>
       *
       * <pre>
       * swrk only 
       * </pre>
       */
      public boolean getRstSibling() {
        return rstSibling_;
      }

      /**
       * <code>optional bool rst_sibling = 26;</code>
       *
       * <pre>
       * swrk only 
       * </pre>
       */
      public Builder setRstSibling(boolean value) {
        bitField0_ |= 0x02000000;
        rstSibling_ = value;
        onChanged();
        return this;
      }

      /**
       * <code>optional bool rst_sibling = 26;</code>
       *
       * <pre>
       * swrk only 
       * </pre>
       */
      public Builder clearRstSibling() {
        bitField0_ = (bitField0_ & ~0x02000000);
        rstSibling_ = false;
        onChanged();
        return this;
      }

      private int appNum_;

      /**
       * <code>required int32 app_num = 27;</code>
       */
      public boolean hasAppNum() {
        return ((bitField0_ & 0x04000000) == 0x04000000);
      }

      /**
       * <code>required int32 app_num = 27;</code>
       */
      public int getAppNum() {
        return appNum_;
      }

      /**
       * <code>required int32 app_num = 27;</code>
       */
      public Builder setAppNum(int value) {
        bitField0_ |= 0x04000000;
        appNum_ = value;
        onChanged();
        return this;
      }

      /**
       * <code>required int32 app_num = 27;</code>
       */
      public Builder clearAppNum() {
        bitField0_ = (bitField0_ & ~0x04000000);
        appNum_ = 0;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:criu_opts)
    }

    static {
      defaultInstance = new criu_opts(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:criu_opts)
  }

  public interface criu_dump_respOrBuilder extends
                                           // @@protoc_insertion_point(interface_extends:criu_dump_resp)
                                                   com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional bool restored = 1;</code>
     */
    boolean hasRestored();

    /**
     * <code>optional bool restored = 1;</code>
     */
    boolean getRestored();
  }

  /**
   * Protobuf type {@code criu_dump_resp}
   */
  public static final class criu_dump_resp extends
                                           com.google.protobuf.GeneratedMessage implements
                                                                                // @@protoc_insertion_point
                                                                                // (message_implements:criu_dump_resp)
                                                                                        criu_dump_respOrBuilder {
    // Use criu_dump_resp.newBuilder() to construct.
    private criu_dump_resp(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }

    private criu_dump_resp(boolean noInit) {
      this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }

    private static final criu_dump_resp defaultInstance;

    public static criu_dump_resp getDefaultInstance() {
      return defaultInstance;
    }

    public criu_dump_resp getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }

    private criu_dump_resp(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
              com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 8: {
              bitField0_ |= 0x00000001;
              restored_ = input.readBool();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
                e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }

    public static final com.google.protobuf.Descriptors.Descriptor
    getDescriptor() {
      return CriuRPC.internal_static_criu_dump_resp_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
    internalGetFieldAccessorTable() {
      return CriuRPC.internal_static_criu_dump_resp_fieldAccessorTable
              .ensureFieldAccessorsInitialized(
                      CriuRPC.criu_dump_resp.class, CriuRPC.criu_dump_resp.Builder.class);
    }

    public static com.google.protobuf.Parser<criu_dump_resp> PARSER =
            new com.google.protobuf.AbstractParser<criu_dump_resp>() {
              public criu_dump_resp parsePartialFrom(
                      com.google.protobuf.CodedInputStream input,
                      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                      throws com.google.protobuf.InvalidProtocolBufferException {
                return new criu_dump_resp(input, extensionRegistry);
              }
            };

    @java.lang.Override
    public com.google.protobuf.Parser<criu_dump_resp> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int RESTORED_FIELD_NUMBER = 1;
    private boolean restored_;

    /**
     * <code>optional bool restored = 1;</code>
     */
    public boolean hasRestored() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }

    /**
     * <code>optional bool restored = 1;</code>
     */
    public boolean getRestored() {
      return restored_;
    }

    private void initFields() {
      restored_ = false;
    }

    private byte memoizedIsInitialized = -1;

    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
            throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBool(1, restored_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;

    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
                .computeBoolSize(1, restored_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;

    @java.lang.Override
    protected java.lang.Object writeReplace()
            throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CriuRPC.criu_dump_resp parseFrom(
            com.google.protobuf.ByteString data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static CriuRPC.criu_dump_resp parseFrom(
            com.google.protobuf.ByteString data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static CriuRPC.criu_dump_resp parseFrom(byte[] data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static CriuRPC.criu_dump_resp parseFrom(
            byte[] data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static CriuRPC.criu_dump_resp parseFrom(java.io.InputStream input)
            throws java.io.IOException {
      return PARSER.parseFrom(input);
    }

    public static CriuRPC.criu_dump_resp parseFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static CriuRPC.criu_dump_resp parseDelimitedFrom(java.io.InputStream input)
            throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }

    public static CriuRPC.criu_dump_resp parseDelimitedFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }

    public static CriuRPC.criu_dump_resp parseFrom(
            com.google.protobuf.CodedInputStream input)
            throws java.io.IOException {
      return PARSER.parseFrom(input);
    }

    public static CriuRPC.criu_dump_resp parseFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() {
      return Builder.create();
    }

    public Builder newBuilderForType() {
      return newBuilder();
    }

    public static Builder newBuilder(CriuRPC.criu_dump_resp prototype) {
      return newBuilder().mergeFrom(prototype);
    }

    public Builder toBuilder() {
      return newBuilder(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
            com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }

    /**
     * Protobuf type {@code criu_dump_resp}
     */
    public static final class Builder extends
                                      com.google.protobuf.GeneratedMessage.Builder<Builder> implements
                                                                                            // @@protoc_insertion_point(builder_implements:criu_dump_resp)
                                                                                                    CriuRPC.criu_dump_respOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
        return CriuRPC.internal_static_criu_dump_resp_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internalGetFieldAccessorTable() {
        return CriuRPC.internal_static_criu_dump_resp_fieldAccessorTable
                .ensureFieldAccessorsInitialized(
                        CriuRPC.criu_dump_resp.class, CriuRPC.criu_dump_resp.Builder.class);
      }

      // Construct using Rpc.criu_dump_resp.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
              com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }

      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }

      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        restored_ = false;
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
      getDescriptorForType() {
        return CriuRPC.internal_static_criu_dump_resp_descriptor;
      }

      public CriuRPC.criu_dump_resp getDefaultInstanceForType() {
        return CriuRPC.criu_dump_resp.getDefaultInstance();
      }

      public CriuRPC.criu_dump_resp build() {
        CriuRPC.criu_dump_resp result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CriuRPC.criu_dump_resp buildPartial() {
        CriuRPC.criu_dump_resp result = new CriuRPC.criu_dump_resp(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.restored_ = restored_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CriuRPC.criu_dump_resp) {
          return mergeFrom((CriuRPC.criu_dump_resp) other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CriuRPC.criu_dump_resp other) {
        if (other == CriuRPC.criu_dump_resp.getDefaultInstance()) return this;
        if (other.hasRestored()) {
          setRestored(other.getRestored());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
              com.google.protobuf.CodedInputStream input,
              com.google.protobuf.ExtensionRegistryLite extensionRegistry)
              throws java.io.IOException {
        CriuRPC.criu_dump_resp parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CriuRPC.criu_dump_resp) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private int bitField0_;

      private boolean restored_;

      /**
       * <code>optional bool restored = 1;</code>
       */
      public boolean hasRestored() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }

      /**
       * <code>optional bool restored = 1;</code>
       */
      public boolean getRestored() {
        return restored_;
      }

      /**
       * <code>optional bool restored = 1;</code>
       */
      public Builder setRestored(boolean value) {
        bitField0_ |= 0x00000001;
        restored_ = value;
        onChanged();
        return this;
      }

      /**
       * <code>optional bool restored = 1;</code>
       */
      public Builder clearRestored() {
        bitField0_ = (bitField0_ & ~0x00000001);
        restored_ = false;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:criu_dump_resp)
    }

    static {
      defaultInstance = new criu_dump_resp(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:criu_dump_resp)
  }

  public interface criu_restore_respOrBuilder extends
                                              // @@protoc_insertion_point(interface_extends:criu_restore_resp)
                                                      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>required int32 pid = 1;</code>
     */
    boolean hasPid();

    /**
     * <code>required int32 pid = 1;</code>
     */
    int getPid();
  }

  /**
   * Protobuf type {@code criu_restore_resp}
   */
  public static final class criu_restore_resp extends
                                              com.google.protobuf.GeneratedMessage implements
                                                                                   // @@protoc_insertion_point
                                                                                   // (message_implements:criu_restore_resp)
                                                                                           criu_restore_respOrBuilder {
    // Use criu_restore_resp.newBuilder() to construct.
    private criu_restore_resp(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }

    private criu_restore_resp(boolean noInit) {
      this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }

    private static final criu_restore_resp defaultInstance;

    public static criu_restore_resp getDefaultInstance() {
      return defaultInstance;
    }

    public criu_restore_resp getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }

    private criu_restore_resp(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
              com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 8: {
              bitField0_ |= 0x00000001;
              pid_ = input.readInt32();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
                e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }

    public static final com.google.protobuf.Descriptors.Descriptor
    getDescriptor() {
      return CriuRPC.internal_static_criu_restore_resp_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
    internalGetFieldAccessorTable() {
      return CriuRPC.internal_static_criu_restore_resp_fieldAccessorTable
              .ensureFieldAccessorsInitialized(
                      CriuRPC.criu_restore_resp.class, CriuRPC.criu_restore_resp.Builder.class);
    }

    public static com.google.protobuf.Parser<criu_restore_resp> PARSER =
            new com.google.protobuf.AbstractParser<criu_restore_resp>() {
              public criu_restore_resp parsePartialFrom(
                      com.google.protobuf.CodedInputStream input,
                      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                      throws com.google.protobuf.InvalidProtocolBufferException {
                return new criu_restore_resp(input, extensionRegistry);
              }
            };

    @java.lang.Override
    public com.google.protobuf.Parser<criu_restore_resp> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int PID_FIELD_NUMBER = 1;
    private int pid_;

    /**
     * <code>required int32 pid = 1;</code>
     */
    public boolean hasPid() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }

    /**
     * <code>required int32 pid = 1;</code>
     */
    public int getPid() {
      return pid_;
    }

    private void initFields() {
      pid_ = 0;
    }

    private byte memoizedIsInitialized = -1;

    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      if (!hasPid()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
            throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeInt32(1, pid_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;

    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
                .computeInt32Size(1, pid_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;

    @java.lang.Override
    protected java.lang.Object writeReplace()
            throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CriuRPC.criu_restore_resp parseFrom(
            com.google.protobuf.ByteString data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static CriuRPC.criu_restore_resp parseFrom(
            com.google.protobuf.ByteString data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static CriuRPC.criu_restore_resp parseFrom(byte[] data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static CriuRPC.criu_restore_resp parseFrom(
            byte[] data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static CriuRPC.criu_restore_resp parseFrom(java.io.InputStream input)
            throws java.io.IOException {
      return PARSER.parseFrom(input);
    }

    public static CriuRPC.criu_restore_resp parseFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static CriuRPC.criu_restore_resp parseDelimitedFrom(java.io.InputStream input)
            throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }

    public static CriuRPC.criu_restore_resp parseDelimitedFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }

    public static CriuRPC.criu_restore_resp parseFrom(
            com.google.protobuf.CodedInputStream input)
            throws java.io.IOException {
      return PARSER.parseFrom(input);
    }

    public static CriuRPC.criu_restore_resp parseFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() {
      return Builder.create();
    }

    public Builder newBuilderForType() {
      return newBuilder();
    }

    public static Builder newBuilder(CriuRPC.criu_restore_resp prototype) {
      return newBuilder().mergeFrom(prototype);
    }

    public Builder toBuilder() {
      return newBuilder(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
            com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }

    /**
     * Protobuf type {@code criu_restore_resp}
     */
    public static final class Builder extends
                                      com.google.protobuf.GeneratedMessage.Builder<Builder> implements
                                                                                            // @@protoc_insertion_point(builder_implements:criu_restore_resp)
                                                                                                    CriuRPC.criu_restore_respOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
        return CriuRPC.internal_static_criu_restore_resp_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internalGetFieldAccessorTable() {
        return CriuRPC.internal_static_criu_restore_resp_fieldAccessorTable
                .ensureFieldAccessorsInitialized(
                        CriuRPC.criu_restore_resp.class, CriuRPC.criu_restore_resp.Builder.class);
      }

      // Construct using Rpc.criu_restore_resp.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
              com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }

      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }

      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        pid_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
      getDescriptorForType() {
        return CriuRPC.internal_static_criu_restore_resp_descriptor;
      }

      public CriuRPC.criu_restore_resp getDefaultInstanceForType() {
        return CriuRPC.criu_restore_resp.getDefaultInstance();
      }

      public CriuRPC.criu_restore_resp build() {
        CriuRPC.criu_restore_resp result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CriuRPC.criu_restore_resp buildPartial() {
        CriuRPC.criu_restore_resp result = new CriuRPC.criu_restore_resp(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.pid_ = pid_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CriuRPC.criu_restore_resp) {
          return mergeFrom((CriuRPC.criu_restore_resp) other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CriuRPC.criu_restore_resp other) {
        if (other == CriuRPC.criu_restore_resp.getDefaultInstance()) return this;
        if (other.hasPid()) {
          setPid(other.getPid());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        if (!hasPid()) {

          return false;
        }
        return true;
      }

      public Builder mergeFrom(
              com.google.protobuf.CodedInputStream input,
              com.google.protobuf.ExtensionRegistryLite extensionRegistry)
              throws java.io.IOException {
        CriuRPC.criu_restore_resp parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CriuRPC.criu_restore_resp) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private int bitField0_;

      private int pid_;

      /**
       * <code>required int32 pid = 1;</code>
       */
      public boolean hasPid() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }

      /**
       * <code>required int32 pid = 1;</code>
       */
      public int getPid() {
        return pid_;
      }

      /**
       * <code>required int32 pid = 1;</code>
       */
      public Builder setPid(int value) {
        bitField0_ |= 0x00000001;
        pid_ = value;
        onChanged();
        return this;
      }

      /**
       * <code>required int32 pid = 1;</code>
       */
      public Builder clearPid() {
        bitField0_ = (bitField0_ & ~0x00000001);
        pid_ = 0;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:criu_restore_resp)
    }

    static {
      defaultInstance = new criu_restore_resp(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:criu_restore_resp)
  }

  public interface criu_notifyOrBuilder extends
                                        // @@protoc_insertion_point(interface_extends:criu_notify)
                                                com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional string script = 1;</code>
     */
    boolean hasScript();

    /**
     * <code>optional string script = 1;</code>
     */
    java.lang.String getScript();

    /**
     * <code>optional string script = 1;</code>
     */
    com.google.protobuf.ByteString
    getScriptBytes();

    /**
     * <code>optional int32 pid = 2;</code>
     */
    boolean hasPid();

    /**
     * <code>optional int32 pid = 2;</code>
     */
    int getPid();
  }

  /**
   * Protobuf type {@code criu_notify}
   */
  public static final class criu_notify extends
                                        com.google.protobuf.GeneratedMessage implements
                                                                             // @@protoc_insertion_point
                                                                             // (message_implements:criu_notify)
                                                                                     criu_notifyOrBuilder {
    // Use criu_notify.newBuilder() to construct.
    private criu_notify(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }

    private criu_notify(boolean noInit) {
      this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }

    private static final criu_notify defaultInstance;

    public static criu_notify getDefaultInstance() {
      return defaultInstance;
    }

    public criu_notify getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }

    private criu_notify(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
              com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000001;
              script_ = bs;
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              pid_ = input.readInt32();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
                e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }

    public static final com.google.protobuf.Descriptors.Descriptor
    getDescriptor() {
      return CriuRPC.internal_static_criu_notify_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
    internalGetFieldAccessorTable() {
      return CriuRPC.internal_static_criu_notify_fieldAccessorTable
              .ensureFieldAccessorsInitialized(
                      CriuRPC.criu_notify.class, CriuRPC.criu_notify.Builder.class);
    }

    public static com.google.protobuf.Parser<criu_notify> PARSER =
            new com.google.protobuf.AbstractParser<criu_notify>() {
              public criu_notify parsePartialFrom(
                      com.google.protobuf.CodedInputStream input,
                      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                      throws com.google.protobuf.InvalidProtocolBufferException {
                return new criu_notify(input, extensionRegistry);
              }
            };

    @java.lang.Override
    public com.google.protobuf.Parser<criu_notify> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int SCRIPT_FIELD_NUMBER = 1;
    private java.lang.Object script_;

    /**
     * <code>optional string script = 1;</code>
     */
    public boolean hasScript() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }

    /**
     * <code>optional string script = 1;</code>
     */
    public java.lang.String getScript() {
      java.lang.Object ref = script_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs =
                (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          script_ = s;
        }
        return s;
      }
    }

    /**
     * <code>optional string script = 1;</code>
     */
    public com.google.protobuf.ByteString
    getScriptBytes() {
      java.lang.Object ref = script_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b =
                com.google.protobuf.ByteString.copyFromUtf8(
                        (java.lang.String) ref);
        script_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int PID_FIELD_NUMBER = 2;
    private int pid_;

    /**
     * <code>optional int32 pid = 2;</code>
     */
    public boolean hasPid() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }

    /**
     * <code>optional int32 pid = 2;</code>
     */
    public int getPid() {
      return pid_;
    }

    private void initFields() {
      script_ = "";
      pid_ = 0;
    }

    private byte memoizedIsInitialized = -1;

    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
            throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, getScriptBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeInt32(2, pid_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;

    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
                .computeBytesSize(1, getScriptBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
                .computeInt32Size(2, pid_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;

    @java.lang.Override
    protected java.lang.Object writeReplace()
            throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CriuRPC.criu_notify parseFrom(
            com.google.protobuf.ByteString data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static CriuRPC.criu_notify parseFrom(
            com.google.protobuf.ByteString data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static CriuRPC.criu_notify parseFrom(byte[] data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static CriuRPC.criu_notify parseFrom(
            byte[] data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static CriuRPC.criu_notify parseFrom(java.io.InputStream input)
            throws java.io.IOException {
      return PARSER.parseFrom(input);
    }

    public static CriuRPC.criu_notify parseFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static CriuRPC.criu_notify parseDelimitedFrom(java.io.InputStream input)
            throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }

    public static CriuRPC.criu_notify parseDelimitedFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }

    public static CriuRPC.criu_notify parseFrom(
            com.google.protobuf.CodedInputStream input)
            throws java.io.IOException {
      return PARSER.parseFrom(input);
    }

    public static CriuRPC.criu_notify parseFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() {
      return Builder.create();
    }

    public Builder newBuilderForType() {
      return newBuilder();
    }

    public static Builder newBuilder(CriuRPC.criu_notify prototype) {
      return newBuilder().mergeFrom(prototype);
    }

    public Builder toBuilder() {
      return newBuilder(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
            com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }

    /**
     * Protobuf type {@code criu_notify}
     */
    public static final class Builder extends
                                      com.google.protobuf.GeneratedMessage.Builder<Builder> implements
                                                                                            // @@protoc_insertion_point(builder_implements:criu_notify)
                                                                                                    CriuRPC.criu_notifyOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
        return CriuRPC.internal_static_criu_notify_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internalGetFieldAccessorTable() {
        return CriuRPC.internal_static_criu_notify_fieldAccessorTable
                .ensureFieldAccessorsInitialized(
                        CriuRPC.criu_notify.class, CriuRPC.criu_notify.Builder.class);
      }

      // Construct using Rpc.criu_notify.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
              com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }

      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }

      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        script_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        pid_ = 0;
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
      getDescriptorForType() {
        return CriuRPC.internal_static_criu_notify_descriptor;
      }

      public CriuRPC.criu_notify getDefaultInstanceForType() {
        return CriuRPC.criu_notify.getDefaultInstance();
      }

      public CriuRPC.criu_notify build() {
        CriuRPC.criu_notify result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CriuRPC.criu_notify buildPartial() {
        CriuRPC.criu_notify result = new CriuRPC.criu_notify(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.script_ = script_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.pid_ = pid_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CriuRPC.criu_notify) {
          return mergeFrom((CriuRPC.criu_notify) other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CriuRPC.criu_notify other) {
        if (other == CriuRPC.criu_notify.getDefaultInstance()) return this;
        if (other.hasScript()) {
          bitField0_ |= 0x00000001;
          script_ = other.script_;
          onChanged();
        }
        if (other.hasPid()) {
          setPid(other.getPid());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
              com.google.protobuf.CodedInputStream input,
              com.google.protobuf.ExtensionRegistryLite extensionRegistry)
              throws java.io.IOException {
        CriuRPC.criu_notify parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CriuRPC.criu_notify) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private int bitField0_;

      private java.lang.Object script_ = "";

      /**
       * <code>optional string script = 1;</code>
       */
      public boolean hasScript() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }

      /**
       * <code>optional string script = 1;</code>
       */
      public java.lang.String getScript() {
        java.lang.Object ref = script_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
                  (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            script_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }

      /**
       * <code>optional string script = 1;</code>
       */
      public com.google.protobuf.ByteString
      getScriptBytes() {
        java.lang.Object ref = script_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b =
                  com.google.protobuf.ByteString.copyFromUtf8(
                          (java.lang.String) ref);
          script_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      /**
       * <code>optional string script = 1;</code>
       */
      public Builder setScript(
              java.lang.String value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000001;
        script_ = value;
        onChanged();
        return this;
      }

      /**
       * <code>optional string script = 1;</code>
       */
      public Builder clearScript() {
        bitField0_ = (bitField0_ & ~0x00000001);
        script_ = getDefaultInstance().getScript();
        onChanged();
        return this;
      }

      /**
       * <code>optional string script = 1;</code>
       */
      public Builder setScriptBytes(
              com.google.protobuf.ByteString value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000001;
        script_ = value;
        onChanged();
        return this;
      }

      private int pid_;

      /**
       * <code>optional int32 pid = 2;</code>
       */
      public boolean hasPid() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }

      /**
       * <code>optional int32 pid = 2;</code>
       */
      public int getPid() {
        return pid_;
      }

      /**
       * <code>optional int32 pid = 2;</code>
       */
      public Builder setPid(int value) {
        bitField0_ |= 0x00000002;
        pid_ = value;
        onChanged();
        return this;
      }

      /**
       * <code>optional int32 pid = 2;</code>
       */
      public Builder clearPid() {
        bitField0_ = (bitField0_ & ~0x00000002);
        pid_ = 0;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:criu_notify)
    }

    static {
      defaultInstance = new criu_notify(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:criu_notify)
  }

  public interface criu_reqOrBuilder extends
                                     // @@protoc_insertion_point(interface_extends:criu_req)
                                             com.google.protobuf.MessageOrBuilder {

    /**
     * <code>required .criu_req_type type = 1;</code>
     */
    boolean hasType();

    /**
     * <code>required .criu_req_type type = 1;</code>
     */
    CriuRPC.criu_req_type getType();

    /**
     * <code>optional .criu_opts opts = 2;</code>
     */
    boolean hasOpts();

    /**
     * <code>optional .criu_opts opts = 2;</code>
     */
    CriuRPC.criu_opts getOpts();

    /**
     * <code>optional .criu_opts opts = 2;</code>
     */
    CriuRPC.criu_optsOrBuilder getOptsOrBuilder();

    /**
     * <code>optional bool notify_success = 3;</code>
     */
    boolean hasNotifySuccess();

    /**
     * <code>optional bool notify_success = 3;</code>
     */
    boolean getNotifySuccess();

    /**
     * <code>optional bool keep_open = 4;</code>
     *
     * <pre>
     * When set service won't close the connection but
     * will wait for more req-s to appear. Works not
     * for all request types.
     * </pre>
     */
    boolean hasKeepOpen();

    /**
     * <code>optional bool keep_open = 4;</code>
     *
     * <pre>
     * When set service won't close the connection but
     * will wait for more req-s to appear. Works not
     * for all request types.
     * </pre>
     */
    boolean getKeepOpen();
  }

  /**
   * Protobuf type {@code criu_req}
   */
  public static final class criu_req extends
                                     com.google.protobuf.GeneratedMessage implements
                                                                          // @@protoc_insertion_point
                                                                          // (message_implements:criu_req)
                                                                                  criu_reqOrBuilder {
    // Use criu_req.newBuilder() to construct.
    private criu_req(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }

    private criu_req(boolean noInit) {
      this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }

    private static final criu_req defaultInstance;

    public static criu_req getDefaultInstance() {
      return defaultInstance;
    }

    public criu_req getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }

    private criu_req(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
              com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 8: {
              int rawValue = input.readEnum();
              CriuRPC.criu_req_type value = CriuRPC.criu_req_type.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(1, rawValue);
              } else {
                bitField0_ |= 0x00000001;
                type_ = value;
              }
              break;
            }
            case 18: {
              CriuRPC.criu_opts.Builder subBuilder = null;
              if (((bitField0_ & 0x00000002) == 0x00000002)) {
                subBuilder = opts_.toBuilder();
              }
              opts_ = input.readMessage(CriuRPC.criu_opts.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(opts_);
                opts_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000002;
              break;
            }
            case 24: {
              bitField0_ |= 0x00000004;
              notifySuccess_ = input.readBool();
              break;
            }
            case 32: {
              bitField0_ |= 0x00000008;
              keepOpen_ = input.readBool();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
                e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }

    public static final com.google.protobuf.Descriptors.Descriptor
    getDescriptor() {
      return CriuRPC.internal_static_criu_req_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
    internalGetFieldAccessorTable() {
      return CriuRPC.internal_static_criu_req_fieldAccessorTable
              .ensureFieldAccessorsInitialized(
                      CriuRPC.criu_req.class, CriuRPC.criu_req.Builder.class);
    }

    public static com.google.protobuf.Parser<criu_req> PARSER =
            new com.google.protobuf.AbstractParser<criu_req>() {
              public criu_req parsePartialFrom(
                      com.google.protobuf.CodedInputStream input,
                      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                      throws com.google.protobuf.InvalidProtocolBufferException {
                return new criu_req(input, extensionRegistry);
              }
            };

    @java.lang.Override
    public com.google.protobuf.Parser<criu_req> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int TYPE_FIELD_NUMBER = 1;
    private CriuRPC.criu_req_type type_;

    /**
     * <code>required .criu_req_type type = 1;</code>
     */
    public boolean hasType() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }

    /**
     * <code>required .criu_req_type type = 1;</code>
     */
    public CriuRPC.criu_req_type getType() {
      return type_;
    }

    public static final int OPTS_FIELD_NUMBER = 2;
    private CriuRPC.criu_opts opts_;

    /**
     * <code>optional .criu_opts opts = 2;</code>
     */
    public boolean hasOpts() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }

    /**
     * <code>optional .criu_opts opts = 2;</code>
     */
    public CriuRPC.criu_opts getOpts() {
      return opts_;
    }

    /**
     * <code>optional .criu_opts opts = 2;</code>
     */
    public CriuRPC.criu_optsOrBuilder getOptsOrBuilder() {
      return opts_;
    }

    public static final int NOTIFY_SUCCESS_FIELD_NUMBER = 3;
    private boolean notifySuccess_;

    /**
     * <code>optional bool notify_success = 3;</code>
     */
    public boolean hasNotifySuccess() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }

    /**
     * <code>optional bool notify_success = 3;</code>
     */
    public boolean getNotifySuccess() {
      return notifySuccess_;
    }

    public static final int KEEP_OPEN_FIELD_NUMBER = 4;
    private boolean keepOpen_;

    /**
     * <code>optional bool keep_open = 4;</code>
     *
     * <pre>
     * When set service won't close the connection but
     * will wait for more req-s to appear. Works not
     * for all request types.
     * </pre>
     */
    public boolean hasKeepOpen() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }

    /**
     * <code>optional bool keep_open = 4;</code>
     *
     * <pre>
     * When set service won't close the connection but
     * will wait for more req-s to appear. Works not
     * for all request types.
     * </pre>
     */
    public boolean getKeepOpen() {
      return keepOpen_;
    }

    private void initFields() {
      type_ = CriuRPC.criu_req_type.EMPTY;
      opts_ = CriuRPC.criu_opts.getDefaultInstance();
      notifySuccess_ = false;
      keepOpen_ = false;
    }

    private byte memoizedIsInitialized = -1;

    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      if (!hasType()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (hasOpts()) {
        if (!getOpts().isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
            throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeEnum(1, type_.getNumber());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeMessage(2, opts_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeBool(3, notifySuccess_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeBool(4, keepOpen_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;

    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
                .computeEnumSize(1, type_.getNumber());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
                .computeMessageSize(2, opts_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
                .computeBoolSize(3, notifySuccess_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
                .computeBoolSize(4, keepOpen_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;

    @java.lang.Override
    protected java.lang.Object writeReplace()
            throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CriuRPC.criu_req parseFrom(
            com.google.protobuf.ByteString data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static CriuRPC.criu_req parseFrom(
            com.google.protobuf.ByteString data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static CriuRPC.criu_req parseFrom(byte[] data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static CriuRPC.criu_req parseFrom(
            byte[] data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static CriuRPC.criu_req parseFrom(java.io.InputStream input)
            throws java.io.IOException {
      return PARSER.parseFrom(input);
    }

    public static CriuRPC.criu_req parseFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static CriuRPC.criu_req parseDelimitedFrom(java.io.InputStream input)
            throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }

    public static CriuRPC.criu_req parseDelimitedFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }

    public static CriuRPC.criu_req parseFrom(
            com.google.protobuf.CodedInputStream input)
            throws java.io.IOException {
      return PARSER.parseFrom(input);
    }

    public static CriuRPC.criu_req parseFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() {
      return Builder.create();
    }

    public Builder newBuilderForType() {
      return newBuilder();
    }

    public static Builder newBuilder(CriuRPC.criu_req prototype) {
      return newBuilder().mergeFrom(prototype);
    }

    public Builder toBuilder() {
      return newBuilder(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
            com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }

    /**
     * Protobuf type {@code criu_req}
     */
    public static final class Builder extends
                                      com.google.protobuf.GeneratedMessage.Builder<Builder> implements
                                                                                            // @@protoc_insertion_point(builder_implements:criu_req)
                                                                                                    CriuRPC.criu_reqOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
        return CriuRPC.internal_static_criu_req_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internalGetFieldAccessorTable() {
        return CriuRPC.internal_static_criu_req_fieldAccessorTable
                .ensureFieldAccessorsInitialized(
                        CriuRPC.criu_req.class, CriuRPC.criu_req.Builder.class);
      }

      // Construct using Rpc.criu_req.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
              com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }

      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getOptsFieldBuilder();
        }
      }

      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        type_ = CriuRPC.criu_req_type.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000001);
        if (optsBuilder_ == null) {
          opts_ = CriuRPC.criu_opts.getDefaultInstance();
        } else {
          optsBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        notifySuccess_ = false;
        bitField0_ = (bitField0_ & ~0x00000004);
        keepOpen_ = false;
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
      getDescriptorForType() {
        return CriuRPC.internal_static_criu_req_descriptor;
      }

      public CriuRPC.criu_req getDefaultInstanceForType() {
        return CriuRPC.criu_req.getDefaultInstance();
      }

      public CriuRPC.criu_req build() {
        CriuRPC.criu_req result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CriuRPC.criu_req buildPartial() {
        CriuRPC.criu_req result = new CriuRPC.criu_req(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.type_ = type_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        if (optsBuilder_ == null) {
          result.opts_ = opts_;
        } else {
          result.opts_ = optsBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.notifySuccess_ = notifySuccess_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.keepOpen_ = keepOpen_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CriuRPC.criu_req) {
          return mergeFrom((CriuRPC.criu_req) other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CriuRPC.criu_req other) {
        if (other == CriuRPC.criu_req.getDefaultInstance()) return this;
        if (other.hasType()) {
          setType(other.getType());
        }
        if (other.hasOpts()) {
          mergeOpts(other.getOpts());
        }
        if (other.hasNotifySuccess()) {
          setNotifySuccess(other.getNotifySuccess());
        }
        if (other.hasKeepOpen()) {
          setKeepOpen(other.getKeepOpen());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        if (!hasType()) {

          return false;
        }
        if (hasOpts()) {
          if (!getOpts().isInitialized()) {

            return false;
          }
        }
        return true;
      }

      public Builder mergeFrom(
              com.google.protobuf.CodedInputStream input,
              com.google.protobuf.ExtensionRegistryLite extensionRegistry)
              throws java.io.IOException {
        CriuRPC.criu_req parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CriuRPC.criu_req) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private int bitField0_;

      private CriuRPC.criu_req_type type_ = CriuRPC.criu_req_type.EMPTY;

      /**
       * <code>required .criu_req_type type = 1;</code>
       */
      public boolean hasType() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }

      /**
       * <code>required .criu_req_type type = 1;</code>
       */
      public CriuRPC.criu_req_type getType() {
        return type_;
      }

      /**
       * <code>required .criu_req_type type = 1;</code>
       */
      public Builder setType(CriuRPC.criu_req_type value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000001;
        type_ = value;
        onChanged();
        return this;
      }

      /**
       * <code>required .criu_req_type type = 1;</code>
       */
      public Builder clearType() {
        bitField0_ = (bitField0_ & ~0x00000001);
        type_ = CriuRPC.criu_req_type.EMPTY;
        onChanged();
        return this;
      }

      private CriuRPC.criu_opts opts_ = CriuRPC.criu_opts.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
              CriuRPC.criu_opts, CriuRPC.criu_opts.Builder, CriuRPC.criu_optsOrBuilder> optsBuilder_;

      /**
       * <code>optional .criu_opts opts = 2;</code>
       */
      public boolean hasOpts() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }

      /**
       * <code>optional .criu_opts opts = 2;</code>
       */
      public CriuRPC.criu_opts getOpts() {
        if (optsBuilder_ == null) {
          return opts_;
        } else {
          return optsBuilder_.getMessage();
        }
      }

      /**
       * <code>optional .criu_opts opts = 2;</code>
       */
      public Builder setOpts(CriuRPC.criu_opts value) {
        if (optsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          opts_ = value;
          onChanged();
        } else {
          optsBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }

      /**
       * <code>optional .criu_opts opts = 2;</code>
       */
      public Builder setOpts(
              CriuRPC.criu_opts.Builder builderForValue) {
        if (optsBuilder_ == null) {
          opts_ = builderForValue.build();
          onChanged();
        } else {
          optsBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000002;
        return this;
      }

      /**
       * <code>optional .criu_opts opts = 2;</code>
       */
      public Builder mergeOpts(CriuRPC.criu_opts value) {
        if (optsBuilder_ == null) {
          if (((bitField0_ & 0x00000002) == 0x00000002) &&
              opts_ != CriuRPC.criu_opts.getDefaultInstance()) {
            opts_ =
                    CriuRPC.criu_opts.newBuilder(opts_).mergeFrom(value).buildPartial();
          } else {
            opts_ = value;
          }
          onChanged();
        } else {
          optsBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }

      /**
       * <code>optional .criu_opts opts = 2;</code>
       */
      public Builder clearOpts() {
        if (optsBuilder_ == null) {
          opts_ = CriuRPC.criu_opts.getDefaultInstance();
          onChanged();
        } else {
          optsBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      /**
       * <code>optional .criu_opts opts = 2;</code>
       */
      public CriuRPC.criu_opts.Builder getOptsBuilder() {
        bitField0_ |= 0x00000002;
        onChanged();
        return getOptsFieldBuilder().getBuilder();
      }

      /**
       * <code>optional .criu_opts opts = 2;</code>
       */
      public CriuRPC.criu_optsOrBuilder getOptsOrBuilder() {
        if (optsBuilder_ != null) {
          return optsBuilder_.getMessageOrBuilder();
        } else {
          return opts_;
        }
      }

      /**
       * <code>optional .criu_opts opts = 2;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
              CriuRPC.criu_opts, CriuRPC.criu_opts.Builder, CriuRPC.criu_optsOrBuilder>
      getOptsFieldBuilder() {
        if (optsBuilder_ == null) {
          optsBuilder_ = new com.google.protobuf.SingleFieldBuilder<
                  CriuRPC.criu_opts, CriuRPC.criu_opts.Builder, CriuRPC.criu_optsOrBuilder>(
                  getOpts(),
                  getParentForChildren(),
                  isClean());
          opts_ = null;
        }
        return optsBuilder_;
      }

      private boolean notifySuccess_;

      /**
       * <code>optional bool notify_success = 3;</code>
       */
      public boolean hasNotifySuccess() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }

      /**
       * <code>optional bool notify_success = 3;</code>
       */
      public boolean getNotifySuccess() {
        return notifySuccess_;
      }

      /**
       * <code>optional bool notify_success = 3;</code>
       */
      public Builder setNotifySuccess(boolean value) {
        bitField0_ |= 0x00000004;
        notifySuccess_ = value;
        onChanged();
        return this;
      }

      /**
       * <code>optional bool notify_success = 3;</code>
       */
      public Builder clearNotifySuccess() {
        bitField0_ = (bitField0_ & ~0x00000004);
        notifySuccess_ = false;
        onChanged();
        return this;
      }

      private boolean keepOpen_;

      /**
       * <code>optional bool keep_open = 4;</code>
       *
       * <pre>
       * When set service won't close the connection but
       * will wait for more req-s to appear. Works not
       * for all request types.
       * </pre>
       */
      public boolean hasKeepOpen() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }

      /**
       * <code>optional bool keep_open = 4;</code>
       *
       * <pre>
       * When set service won't close the connection but
       * will wait for more req-s to appear. Works not
       * for all request types.
       * </pre>
       */
      public boolean getKeepOpen() {
        return keepOpen_;
      }

      /**
       * <code>optional bool keep_open = 4;</code>
       *
       * <pre>
       * When set service won't close the connection but
       * will wait for more req-s to appear. Works not
       * for all request types.
       * </pre>
       */
      public Builder setKeepOpen(boolean value) {
        bitField0_ |= 0x00000008;
        keepOpen_ = value;
        onChanged();
        return this;
      }

      /**
       * <code>optional bool keep_open = 4;</code>
       *
       * <pre>
       * When set service won't close the connection but
       * will wait for more req-s to appear. Works not
       * for all request types.
       * </pre>
       */
      public Builder clearKeepOpen() {
        bitField0_ = (bitField0_ & ~0x00000008);
        keepOpen_ = false;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:criu_req)
    }

    static {
      defaultInstance = new criu_req(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:criu_req)
  }

  public interface criu_respOrBuilder extends
                                      // @@protoc_insertion_point(interface_extends:criu_resp)
                                              com.google.protobuf.MessageOrBuilder {

    /**
     * <code>required .criu_req_type type = 1;</code>
     */
    boolean hasType();

    /**
     * <code>required .criu_req_type type = 1;</code>
     */
    CriuRPC.criu_req_type getType();

    /**
     * <code>required bool success = 2;</code>
     */
    boolean hasSuccess();

    /**
     * <code>required bool success = 2;</code>
     */
    boolean getSuccess();

    /**
     * <code>optional .criu_dump_resp dump = 3;</code>
     */
    boolean hasDump();

    /**
     * <code>optional .criu_dump_resp dump = 3;</code>
     */
    CriuRPC.criu_dump_resp getDump();

    /**
     * <code>optional .criu_dump_resp dump = 3;</code>
     */
    CriuRPC.criu_dump_respOrBuilder getDumpOrBuilder();

    /**
     * <code>optional .criu_restore_resp restore = 4;</code>
     */
    boolean hasRestore();

    /**
     * <code>optional .criu_restore_resp restore = 4;</code>
     */
    CriuRPC.criu_restore_resp getRestore();

    /**
     * <code>optional .criu_restore_resp restore = 4;</code>
     */
    CriuRPC.criu_restore_respOrBuilder getRestoreOrBuilder();

    /**
     * <code>optional .criu_notify notify = 5;</code>
     */
    boolean hasNotify();

    /**
     * <code>optional .criu_notify notify = 5;</code>
     */
    CriuRPC.criu_notify getNotify();

    /**
     * <code>optional .criu_notify notify = 5;</code>
     */
    CriuRPC.criu_notifyOrBuilder getNotifyOrBuilder();

    /**
     * <code>optional .criu_page_server_info ps = 6;</code>
     */
    boolean hasPs();

    /**
     * <code>optional .criu_page_server_info ps = 6;</code>
     */
    CriuRPC.criu_page_server_info getPs();

    /**
     * <code>optional .criu_page_server_info ps = 6;</code>
     */
    CriuRPC.criu_page_server_infoOrBuilder getPsOrBuilder();

    /**
     * <code>optional int32 cr_errno = 7;</code>
     */
    boolean hasCrErrno();

    /**
     * <code>optional int32 cr_errno = 7;</code>
     */
    int getCrErrno();
  }

  /**
   * Protobuf type {@code criu_resp}
   */
  public static final class criu_resp extends
                                      com.google.protobuf.GeneratedMessage implements
                                                                           // @@protoc_insertion_point
                                                                           // (message_implements:criu_resp)
                                                                                   criu_respOrBuilder {
    // Use criu_resp.newBuilder() to construct.
    private criu_resp(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }

    private criu_resp(boolean noInit) {
      this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }

    private static final criu_resp defaultInstance;

    public static criu_resp getDefaultInstance() {
      return defaultInstance;
    }

    public criu_resp getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }

    private criu_resp(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
              com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 8: {
              int rawValue = input.readEnum();
              CriuRPC.criu_req_type value = CriuRPC.criu_req_type.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(1, rawValue);
              } else {
                bitField0_ |= 0x00000001;
                type_ = value;
              }
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              success_ = input.readBool();
              break;
            }
            case 26: {
              CriuRPC.criu_dump_resp.Builder subBuilder = null;
              if (((bitField0_ & 0x00000004) == 0x00000004)) {
                subBuilder = dump_.toBuilder();
              }
              dump_ = input.readMessage(CriuRPC.criu_dump_resp.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(dump_);
                dump_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000004;
              break;
            }
            case 34: {
              CriuRPC.criu_restore_resp.Builder subBuilder = null;
              if (((bitField0_ & 0x00000008) == 0x00000008)) {
                subBuilder = restore_.toBuilder();
              }
              restore_ = input.readMessage(CriuRPC.criu_restore_resp.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(restore_);
                restore_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000008;
              break;
            }
            case 42: {
              CriuRPC.criu_notify.Builder subBuilder = null;
              if (((bitField0_ & 0x00000010) == 0x00000010)) {
                subBuilder = notify_.toBuilder();
              }
              notify_ = input.readMessage(CriuRPC.criu_notify.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(notify_);
                notify_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000010;
              break;
            }
            case 50: {
              CriuRPC.criu_page_server_info.Builder subBuilder = null;
              if (((bitField0_ & 0x00000020) == 0x00000020)) {
                subBuilder = ps_.toBuilder();
              }
              ps_ = input.readMessage(CriuRPC.criu_page_server_info.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(ps_);
                ps_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000020;
              break;
            }
            case 56: {
              bitField0_ |= 0x00000040;
              crErrno_ = input.readInt32();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
                e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }

    public static final com.google.protobuf.Descriptors.Descriptor
    getDescriptor() {
      return CriuRPC.internal_static_criu_resp_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
    internalGetFieldAccessorTable() {
      return CriuRPC.internal_static_criu_resp_fieldAccessorTable
              .ensureFieldAccessorsInitialized(
                      CriuRPC.criu_resp.class, CriuRPC.criu_resp.Builder.class);
    }

    public static com.google.protobuf.Parser<criu_resp> PARSER =
            new com.google.protobuf.AbstractParser<criu_resp>() {
              public criu_resp parsePartialFrom(
                      com.google.protobuf.CodedInputStream input,
                      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                      throws com.google.protobuf.InvalidProtocolBufferException {
                return new criu_resp(input, extensionRegistry);
              }
            };

    @java.lang.Override
    public com.google.protobuf.Parser<criu_resp> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int TYPE_FIELD_NUMBER = 1;
    private CriuRPC.criu_req_type type_;

    /**
     * <code>required .criu_req_type type = 1;</code>
     */
    public boolean hasType() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }

    /**
     * <code>required .criu_req_type type = 1;</code>
     */
    public CriuRPC.criu_req_type getType() {
      return type_;
    }

    public static final int SUCCESS_FIELD_NUMBER = 2;
    private boolean success_;

    /**
     * <code>required bool success = 2;</code>
     */
    public boolean hasSuccess() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }

    /**
     * <code>required bool success = 2;</code>
     */
    public boolean getSuccess() {
      return success_;
    }

    public static final int DUMP_FIELD_NUMBER = 3;
    private CriuRPC.criu_dump_resp dump_;

    /**
     * <code>optional .criu_dump_resp dump = 3;</code>
     */
    public boolean hasDump() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }

    /**
     * <code>optional .criu_dump_resp dump = 3;</code>
     */
    public CriuRPC.criu_dump_resp getDump() {
      return dump_;
    }

    /**
     * <code>optional .criu_dump_resp dump = 3;</code>
     */
    public CriuRPC.criu_dump_respOrBuilder getDumpOrBuilder() {
      return dump_;
    }

    public static final int RESTORE_FIELD_NUMBER = 4;
    private CriuRPC.criu_restore_resp restore_;

    /**
     * <code>optional .criu_restore_resp restore = 4;</code>
     */
    public boolean hasRestore() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }

    /**
     * <code>optional .criu_restore_resp restore = 4;</code>
     */
    public CriuRPC.criu_restore_resp getRestore() {
      return restore_;
    }

    /**
     * <code>optional .criu_restore_resp restore = 4;</code>
     */
    public CriuRPC.criu_restore_respOrBuilder getRestoreOrBuilder() {
      return restore_;
    }

    public static final int NOTIFY_FIELD_NUMBER = 5;
    private CriuRPC.criu_notify notify_;

    /**
     * <code>optional .criu_notify notify = 5;</code>
     */
    public boolean hasNotify() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }

    /**
     * <code>optional .criu_notify notify = 5;</code>
     */
    public CriuRPC.criu_notify getNotify() {
      return notify_;
    }

    /**
     * <code>optional .criu_notify notify = 5;</code>
     */
    public CriuRPC.criu_notifyOrBuilder getNotifyOrBuilder() {
      return notify_;
    }

    public static final int PS_FIELD_NUMBER = 6;
    private CriuRPC.criu_page_server_info ps_;

    /**
     * <code>optional .criu_page_server_info ps = 6;</code>
     */
    public boolean hasPs() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }

    /**
     * <code>optional .criu_page_server_info ps = 6;</code>
     */
    public CriuRPC.criu_page_server_info getPs() {
      return ps_;
    }

    /**
     * <code>optional .criu_page_server_info ps = 6;</code>
     */
    public CriuRPC.criu_page_server_infoOrBuilder getPsOrBuilder() {
      return ps_;
    }

    public static final int CR_ERRNO_FIELD_NUMBER = 7;
    private int crErrno_;

    /**
     * <code>optional int32 cr_errno = 7;</code>
     */
    public boolean hasCrErrno() {
      return ((bitField0_ & 0x00000040) == 0x00000040);
    }

    /**
     * <code>optional int32 cr_errno = 7;</code>
     */
    public int getCrErrno() {
      return crErrno_;
    }

    private void initFields() {
      type_ = CriuRPC.criu_req_type.EMPTY;
      success_ = false;
      dump_ = CriuRPC.criu_dump_resp.getDefaultInstance();
      restore_ = CriuRPC.criu_restore_resp.getDefaultInstance();
      notify_ = CriuRPC.criu_notify.getDefaultInstance();
      ps_ = CriuRPC.criu_page_server_info.getDefaultInstance();
      crErrno_ = 0;
    }

    private byte memoizedIsInitialized = -1;

    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      if (!hasType()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasSuccess()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (hasRestore()) {
        if (!getRestore().isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
            throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeEnum(1, type_.getNumber());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBool(2, success_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeMessage(3, dump_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeMessage(4, restore_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeMessage(5, notify_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeMessage(6, ps_);
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        output.writeInt32(7, crErrno_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;

    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
                .computeEnumSize(1, type_.getNumber());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
                .computeBoolSize(2, success_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
                .computeMessageSize(3, dump_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
                .computeMessageSize(4, restore_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
                .computeMessageSize(5, notify_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
                .computeMessageSize(6, ps_);
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        size += com.google.protobuf.CodedOutputStream
                .computeInt32Size(7, crErrno_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;

    @java.lang.Override
    protected java.lang.Object writeReplace()
            throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CriuRPC.criu_resp parseFrom(
            com.google.protobuf.ByteString data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static CriuRPC.criu_resp parseFrom(
            com.google.protobuf.ByteString data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static CriuRPC.criu_resp parseFrom(byte[] data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static CriuRPC.criu_resp parseFrom(
            byte[] data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static CriuRPC.criu_resp parseFrom(java.io.InputStream input)
            throws java.io.IOException {
      return PARSER.parseFrom(input);
    }

    public static CriuRPC.criu_resp parseFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static CriuRPC.criu_resp parseDelimitedFrom(java.io.InputStream input)
            throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }

    public static CriuRPC.criu_resp parseDelimitedFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }

    public static CriuRPC.criu_resp parseFrom(
            com.google.protobuf.CodedInputStream input)
            throws java.io.IOException {
      return PARSER.parseFrom(input);
    }

    public static CriuRPC.criu_resp parseFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() {
      return Builder.create();
    }

    public Builder newBuilderForType() {
      return newBuilder();
    }

    public static Builder newBuilder(CriuRPC.criu_resp prototype) {
      return newBuilder().mergeFrom(prototype);
    }

    public Builder toBuilder() {
      return newBuilder(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
            com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }

    /**
     * Protobuf type {@code criu_resp}
     */
    public static final class Builder extends
                                      com.google.protobuf.GeneratedMessage.Builder<Builder> implements
                                                                                            // @@protoc_insertion_point(builder_implements:criu_resp)
                                                                                                    CriuRPC.criu_respOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
        return CriuRPC.internal_static_criu_resp_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internalGetFieldAccessorTable() {
        return CriuRPC.internal_static_criu_resp_fieldAccessorTable
                .ensureFieldAccessorsInitialized(
                        CriuRPC.criu_resp.class, CriuRPC.criu_resp.Builder.class);
      }

      // Construct using Rpc.criu_resp.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
              com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }

      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getDumpFieldBuilder();
          getRestoreFieldBuilder();
          getNotifyFieldBuilder();
          getPsFieldBuilder();
        }
      }

      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        type_ = CriuRPC.criu_req_type.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000001);
        success_ = false;
        bitField0_ = (bitField0_ & ~0x00000002);
        if (dumpBuilder_ == null) {
          dump_ = CriuRPC.criu_dump_resp.getDefaultInstance();
        } else {
          dumpBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        if (restoreBuilder_ == null) {
          restore_ = CriuRPC.criu_restore_resp.getDefaultInstance();
        } else {
          restoreBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000008);
        if (notifyBuilder_ == null) {
          notify_ = CriuRPC.criu_notify.getDefaultInstance();
        } else {
          notifyBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000010);
        if (psBuilder_ == null) {
          ps_ = CriuRPC.criu_page_server_info.getDefaultInstance();
        } else {
          psBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000020);
        crErrno_ = 0;
        bitField0_ = (bitField0_ & ~0x00000040);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
      getDescriptorForType() {
        return CriuRPC.internal_static_criu_resp_descriptor;
      }

      public CriuRPC.criu_resp getDefaultInstanceForType() {
        return CriuRPC.criu_resp.getDefaultInstance();
      }

      public CriuRPC.criu_resp build() {
        CriuRPC.criu_resp result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CriuRPC.criu_resp buildPartial() {
        CriuRPC.criu_resp result = new CriuRPC.criu_resp(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.type_ = type_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.success_ = success_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        if (dumpBuilder_ == null) {
          result.dump_ = dump_;
        } else {
          result.dump_ = dumpBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        if (restoreBuilder_ == null) {
          result.restore_ = restore_;
        } else {
          result.restore_ = restoreBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        if (notifyBuilder_ == null) {
          result.notify_ = notify_;
        } else {
          result.notify_ = notifyBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000020) == 0x00000020)) {
          to_bitField0_ |= 0x00000020;
        }
        if (psBuilder_ == null) {
          result.ps_ = ps_;
        } else {
          result.ps_ = psBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000040) == 0x00000040)) {
          to_bitField0_ |= 0x00000040;
        }
        result.crErrno_ = crErrno_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CriuRPC.criu_resp) {
          return mergeFrom((CriuRPC.criu_resp) other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CriuRPC.criu_resp other) {
        if (other == CriuRPC.criu_resp.getDefaultInstance()) return this;
        if (other.hasType()) {
          setType(other.getType());
        }
        if (other.hasSuccess()) {
          setSuccess(other.getSuccess());
        }
        if (other.hasDump()) {
          mergeDump(other.getDump());
        }
        if (other.hasRestore()) {
          mergeRestore(other.getRestore());
        }
        if (other.hasNotify()) {
          mergeNotify(other.getNotify());
        }
        if (other.hasPs()) {
          mergePs(other.getPs());
        }
        if (other.hasCrErrno()) {
          setCrErrno(other.getCrErrno());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        if (!hasType()) {

          return false;
        }
        if (!hasSuccess()) {

          return false;
        }
        if (hasRestore()) {
          if (!getRestore().isInitialized()) {

            return false;
          }
        }
        return true;
      }

      public Builder mergeFrom(
              com.google.protobuf.CodedInputStream input,
              com.google.protobuf.ExtensionRegistryLite extensionRegistry)
              throws java.io.IOException {
        CriuRPC.criu_resp parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CriuRPC.criu_resp) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private int bitField0_;

      private CriuRPC.criu_req_type type_ = CriuRPC.criu_req_type.EMPTY;

      /**
       * <code>required .criu_req_type type = 1;</code>
       */
      public boolean hasType() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }

      /**
       * <code>required .criu_req_type type = 1;</code>
       */
      public CriuRPC.criu_req_type getType() {
        return type_;
      }

      /**
       * <code>required .criu_req_type type = 1;</code>
       */
      public Builder setType(CriuRPC.criu_req_type value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000001;
        type_ = value;
        onChanged();
        return this;
      }

      /**
       * <code>required .criu_req_type type = 1;</code>
       */
      public Builder clearType() {
        bitField0_ = (bitField0_ & ~0x00000001);
        type_ = CriuRPC.criu_req_type.EMPTY;
        onChanged();
        return this;
      }

      private boolean success_;

      /**
       * <code>required bool success = 2;</code>
       */
      public boolean hasSuccess() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }

      /**
       * <code>required bool success = 2;</code>
       */
      public boolean getSuccess() {
        return success_;
      }

      /**
       * <code>required bool success = 2;</code>
       */
      public Builder setSuccess(boolean value) {
        bitField0_ |= 0x00000002;
        success_ = value;
        onChanged();
        return this;
      }

      /**
       * <code>required bool success = 2;</code>
       */
      public Builder clearSuccess() {
        bitField0_ = (bitField0_ & ~0x00000002);
        success_ = false;
        onChanged();
        return this;
      }

      private CriuRPC.criu_dump_resp dump_ = CriuRPC.criu_dump_resp.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
              CriuRPC.criu_dump_resp, CriuRPC.criu_dump_resp.Builder, CriuRPC.criu_dump_respOrBuilder> dumpBuilder_;

      /**
       * <code>optional .criu_dump_resp dump = 3;</code>
       */
      public boolean hasDump() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }

      /**
       * <code>optional .criu_dump_resp dump = 3;</code>
       */
      public CriuRPC.criu_dump_resp getDump() {
        if (dumpBuilder_ == null) {
          return dump_;
        } else {
          return dumpBuilder_.getMessage();
        }
      }

      /**
       * <code>optional .criu_dump_resp dump = 3;</code>
       */
      public Builder setDump(CriuRPC.criu_dump_resp value) {
        if (dumpBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          dump_ = value;
          onChanged();
        } else {
          dumpBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000004;
        return this;
      }

      /**
       * <code>optional .criu_dump_resp dump = 3;</code>
       */
      public Builder setDump(
              CriuRPC.criu_dump_resp.Builder builderForValue) {
        if (dumpBuilder_ == null) {
          dump_ = builderForValue.build();
          onChanged();
        } else {
          dumpBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000004;
        return this;
      }

      /**
       * <code>optional .criu_dump_resp dump = 3;</code>
       */
      public Builder mergeDump(CriuRPC.criu_dump_resp value) {
        if (dumpBuilder_ == null) {
          if (((bitField0_ & 0x00000004) == 0x00000004) &&
              dump_ != CriuRPC.criu_dump_resp.getDefaultInstance()) {
            dump_ =
                    CriuRPC.criu_dump_resp.newBuilder(dump_).mergeFrom(value).buildPartial();
          } else {
            dump_ = value;
          }
          onChanged();
        } else {
          dumpBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000004;
        return this;
      }

      /**
       * <code>optional .criu_dump_resp dump = 3;</code>
       */
      public Builder clearDump() {
        if (dumpBuilder_ == null) {
          dump_ = CriuRPC.criu_dump_resp.getDefaultInstance();
          onChanged();
        } else {
          dumpBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }

      /**
       * <code>optional .criu_dump_resp dump = 3;</code>
       */
      public CriuRPC.criu_dump_resp.Builder getDumpBuilder() {
        bitField0_ |= 0x00000004;
        onChanged();
        return getDumpFieldBuilder().getBuilder();
      }

      /**
       * <code>optional .criu_dump_resp dump = 3;</code>
       */
      public CriuRPC.criu_dump_respOrBuilder getDumpOrBuilder() {
        if (dumpBuilder_ != null) {
          return dumpBuilder_.getMessageOrBuilder();
        } else {
          return dump_;
        }
      }

      /**
       * <code>optional .criu_dump_resp dump = 3;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
              CriuRPC.criu_dump_resp, CriuRPC.criu_dump_resp.Builder, CriuRPC.criu_dump_respOrBuilder>
      getDumpFieldBuilder() {
        if (dumpBuilder_ == null) {
          dumpBuilder_ = new com.google.protobuf.SingleFieldBuilder<
                  CriuRPC.criu_dump_resp, CriuRPC.criu_dump_resp.Builder, CriuRPC.criu_dump_respOrBuilder>(
                  getDump(),
                  getParentForChildren(),
                  isClean());
          dump_ = null;
        }
        return dumpBuilder_;
      }

      private CriuRPC.criu_restore_resp restore_ = CriuRPC.criu_restore_resp.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
              CriuRPC.criu_restore_resp, CriuRPC.criu_restore_resp.Builder, CriuRPC.criu_restore_respOrBuilder>
              restoreBuilder_;

      /**
       * <code>optional .criu_restore_resp restore = 4;</code>
       */
      public boolean hasRestore() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }

      /**
       * <code>optional .criu_restore_resp restore = 4;</code>
       */
      public CriuRPC.criu_restore_resp getRestore() {
        if (restoreBuilder_ == null) {
          return restore_;
        } else {
          return restoreBuilder_.getMessage();
        }
      }

      /**
       * <code>optional .criu_restore_resp restore = 4;</code>
       */
      public Builder setRestore(CriuRPC.criu_restore_resp value) {
        if (restoreBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          restore_ = value;
          onChanged();
        } else {
          restoreBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000008;
        return this;
      }

      /**
       * <code>optional .criu_restore_resp restore = 4;</code>
       */
      public Builder setRestore(
              CriuRPC.criu_restore_resp.Builder builderForValue) {
        if (restoreBuilder_ == null) {
          restore_ = builderForValue.build();
          onChanged();
        } else {
          restoreBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000008;
        return this;
      }

      /**
       * <code>optional .criu_restore_resp restore = 4;</code>
       */
      public Builder mergeRestore(CriuRPC.criu_restore_resp value) {
        if (restoreBuilder_ == null) {
          if (((bitField0_ & 0x00000008) == 0x00000008) &&
              restore_ != CriuRPC.criu_restore_resp.getDefaultInstance()) {
            restore_ =
                    CriuRPC.criu_restore_resp.newBuilder(restore_).mergeFrom(value).buildPartial();
          } else {
            restore_ = value;
          }
          onChanged();
        } else {
          restoreBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000008;
        return this;
      }

      /**
       * <code>optional .criu_restore_resp restore = 4;</code>
       */
      public Builder clearRestore() {
        if (restoreBuilder_ == null) {
          restore_ = CriuRPC.criu_restore_resp.getDefaultInstance();
          onChanged();
        } else {
          restoreBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }

      /**
       * <code>optional .criu_restore_resp restore = 4;</code>
       */
      public CriuRPC.criu_restore_resp.Builder getRestoreBuilder() {
        bitField0_ |= 0x00000008;
        onChanged();
        return getRestoreFieldBuilder().getBuilder();
      }

      /**
       * <code>optional .criu_restore_resp restore = 4;</code>
       */
      public CriuRPC.criu_restore_respOrBuilder getRestoreOrBuilder() {
        if (restoreBuilder_ != null) {
          return restoreBuilder_.getMessageOrBuilder();
        } else {
          return restore_;
        }
      }

      /**
       * <code>optional .criu_restore_resp restore = 4;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
              CriuRPC.criu_restore_resp, CriuRPC.criu_restore_resp.Builder, CriuRPC.criu_restore_respOrBuilder>
      getRestoreFieldBuilder() {
        if (restoreBuilder_ == null) {
          restoreBuilder_ = new com.google.protobuf.SingleFieldBuilder<
                  CriuRPC.criu_restore_resp, CriuRPC.criu_restore_resp.Builder, CriuRPC.criu_restore_respOrBuilder>(
                  getRestore(),
                  getParentForChildren(),
                  isClean());
          restore_ = null;
        }
        return restoreBuilder_;
      }

      private CriuRPC.criu_notify notify_ = CriuRPC.criu_notify.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
              CriuRPC.criu_notify, CriuRPC.criu_notify.Builder, CriuRPC.criu_notifyOrBuilder> notifyBuilder_;

      /**
       * <code>optional .criu_notify notify = 5;</code>
       */
      public boolean hasNotify() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }

      /**
       * <code>optional .criu_notify notify = 5;</code>
       */
      public CriuRPC.criu_notify getNotify() {
        if (notifyBuilder_ == null) {
          return notify_;
        } else {
          return notifyBuilder_.getMessage();
        }
      }

      /**
       * <code>optional .criu_notify notify = 5;</code>
       */
      public Builder setNotify(CriuRPC.criu_notify value) {
        if (notifyBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          notify_ = value;
          onChanged();
        } else {
          notifyBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000010;
        return this;
      }

      /**
       * <code>optional .criu_notify notify = 5;</code>
       */
      public Builder setNotify(
              CriuRPC.criu_notify.Builder builderForValue) {
        if (notifyBuilder_ == null) {
          notify_ = builderForValue.build();
          onChanged();
        } else {
          notifyBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000010;
        return this;
      }

      /**
       * <code>optional .criu_notify notify = 5;</code>
       */
      public Builder mergeNotify(CriuRPC.criu_notify value) {
        if (notifyBuilder_ == null) {
          if (((bitField0_ & 0x00000010) == 0x00000010) &&
              notify_ != CriuRPC.criu_notify.getDefaultInstance()) {
            notify_ =
                    CriuRPC.criu_notify.newBuilder(notify_).mergeFrom(value).buildPartial();
          } else {
            notify_ = value;
          }
          onChanged();
        } else {
          notifyBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000010;
        return this;
      }

      /**
       * <code>optional .criu_notify notify = 5;</code>
       */
      public Builder clearNotify() {
        if (notifyBuilder_ == null) {
          notify_ = CriuRPC.criu_notify.getDefaultInstance();
          onChanged();
        } else {
          notifyBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000010);
        return this;
      }

      /**
       * <code>optional .criu_notify notify = 5;</code>
       */
      public CriuRPC.criu_notify.Builder getNotifyBuilder() {
        bitField0_ |= 0x00000010;
        onChanged();
        return getNotifyFieldBuilder().getBuilder();
      }

      /**
       * <code>optional .criu_notify notify = 5;</code>
       */
      public CriuRPC.criu_notifyOrBuilder getNotifyOrBuilder() {
        if (notifyBuilder_ != null) {
          return notifyBuilder_.getMessageOrBuilder();
        } else {
          return notify_;
        }
      }

      /**
       * <code>optional .criu_notify notify = 5;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
              CriuRPC.criu_notify, CriuRPC.criu_notify.Builder, CriuRPC.criu_notifyOrBuilder>
      getNotifyFieldBuilder() {
        if (notifyBuilder_ == null) {
          notifyBuilder_ = new com.google.protobuf.SingleFieldBuilder<
                  CriuRPC.criu_notify, CriuRPC.criu_notify.Builder, CriuRPC.criu_notifyOrBuilder>(
                  getNotify(),
                  getParentForChildren(),
                  isClean());
          notify_ = null;
        }
        return notifyBuilder_;
      }

      private CriuRPC.criu_page_server_info ps_ = CriuRPC.criu_page_server_info.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
              CriuRPC.criu_page_server_info, CriuRPC.criu_page_server_info.Builder, CriuRPC
              .criu_page_server_infoOrBuilder>
              psBuilder_;

      /**
       * <code>optional .criu_page_server_info ps = 6;</code>
       */
      public boolean hasPs() {
        return ((bitField0_ & 0x00000020) == 0x00000020);
      }

      /**
       * <code>optional .criu_page_server_info ps = 6;</code>
       */
      public CriuRPC.criu_page_server_info getPs() {
        if (psBuilder_ == null) {
          return ps_;
        } else {
          return psBuilder_.getMessage();
        }
      }

      /**
       * <code>optional .criu_page_server_info ps = 6;</code>
       */
      public Builder setPs(CriuRPC.criu_page_server_info value) {
        if (psBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ps_ = value;
          onChanged();
        } else {
          psBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000020;
        return this;
      }

      /**
       * <code>optional .criu_page_server_info ps = 6;</code>
       */
      public Builder setPs(
              CriuRPC.criu_page_server_info.Builder builderForValue) {
        if (psBuilder_ == null) {
          ps_ = builderForValue.build();
          onChanged();
        } else {
          psBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000020;
        return this;
      }

      /**
       * <code>optional .criu_page_server_info ps = 6;</code>
       */
      public Builder mergePs(CriuRPC.criu_page_server_info value) {
        if (psBuilder_ == null) {
          if (((bitField0_ & 0x00000020) == 0x00000020) &&
              ps_ != CriuRPC.criu_page_server_info.getDefaultInstance()) {
            ps_ =
                    CriuRPC.criu_page_server_info.newBuilder(ps_).mergeFrom(value).buildPartial();
          } else {
            ps_ = value;
          }
          onChanged();
        } else {
          psBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000020;
        return this;
      }

      /**
       * <code>optional .criu_page_server_info ps = 6;</code>
       */
      public Builder clearPs() {
        if (psBuilder_ == null) {
          ps_ = CriuRPC.criu_page_server_info.getDefaultInstance();
          onChanged();
        } else {
          psBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000020);
        return this;
      }

      /**
       * <code>optional .criu_page_server_info ps = 6;</code>
       */
      public CriuRPC.criu_page_server_info.Builder getPsBuilder() {
        bitField0_ |= 0x00000020;
        onChanged();
        return getPsFieldBuilder().getBuilder();
      }

      /**
       * <code>optional .criu_page_server_info ps = 6;</code>
       */
      public CriuRPC.criu_page_server_infoOrBuilder getPsOrBuilder() {
        if (psBuilder_ != null) {
          return psBuilder_.getMessageOrBuilder();
        } else {
          return ps_;
        }
      }

      /**
       * <code>optional .criu_page_server_info ps = 6;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
              CriuRPC.criu_page_server_info, CriuRPC.criu_page_server_info.Builder, CriuRPC
              .criu_page_server_infoOrBuilder>
      getPsFieldBuilder() {
        if (psBuilder_ == null) {
          psBuilder_ = new com.google.protobuf.SingleFieldBuilder<
                  CriuRPC.criu_page_server_info, CriuRPC.criu_page_server_info.Builder, CriuRPC
                  .criu_page_server_infoOrBuilder>(
                  getPs(),
                  getParentForChildren(),
                  isClean());
          ps_ = null;
        }
        return psBuilder_;
      }

      private int crErrno_;

      /**
       * <code>optional int32 cr_errno = 7;</code>
       */
      public boolean hasCrErrno() {
        return ((bitField0_ & 0x00000040) == 0x00000040);
      }

      /**
       * <code>optional int32 cr_errno = 7;</code>
       */
      public int getCrErrno() {
        return crErrno_;
      }

      /**
       * <code>optional int32 cr_errno = 7;</code>
       */
      public Builder setCrErrno(int value) {
        bitField0_ |= 0x00000040;
        crErrno_ = value;
        onChanged();
        return this;
      }

      /**
       * <code>optional int32 cr_errno = 7;</code>
       */
      public Builder clearCrErrno() {
        bitField0_ = (bitField0_ & ~0x00000040);
        crErrno_ = 0;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:criu_resp)
    }

    static {
      defaultInstance = new criu_resp(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:criu_resp)
  }

  private static final com.google.protobuf.Descriptors.Descriptor
          internal_static_criu_page_server_info_descriptor;
  private static
  com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internal_static_criu_page_server_info_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
          internal_static_criu_veth_pair_descriptor;
  private static
  com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internal_static_criu_veth_pair_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
          internal_static_ext_mount_map_descriptor;
  private static
  com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internal_static_ext_mount_map_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
          internal_static_cgroup_root_descriptor;
  private static
  com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internal_static_cgroup_root_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
          internal_static_criu_opts_descriptor;
  private static
  com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internal_static_criu_opts_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
          internal_static_criu_dump_resp_descriptor;
  private static
  com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internal_static_criu_dump_resp_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
          internal_static_criu_restore_resp_descriptor;
  private static
  com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internal_static_criu_restore_resp_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
          internal_static_criu_notify_descriptor;
  private static
  com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internal_static_criu_notify_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
          internal_static_criu_req_descriptor;
  private static
  com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internal_static_criu_req_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
          internal_static_criu_resp_descriptor;
  private static
  com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internal_static_criu_resp_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
  getDescriptor() {
    return descriptor;
  }

  private static com.google.protobuf.Descriptors.FileDescriptor
          descriptor;

  static {
    java.lang.String[] descriptorData = {
            "\n+edu/duke/cs/legosdn/tools/cr/criu/rpc." +
            "proto\"O\n\025criu_page_server_info\022\017\n\007addres" +
            "s\030\001 \001(\t\022\014\n\004port\030\002 \001(\005\022\013\n\003pid\030\003 \001(\005\022\n\n\002fd" +
            "\030\004 \001(\005\"/\n\016criu_veth_pair\022\r\n\005if_in\030\001 \002(\t\022" +
            "\016\n\006if_out\030\002 \002(\t\")\n\rext_mount_map\022\013\n\003key\030" +
            "\001 \002(\t\022\013\n\003val\030\002 \002(\t\")\n\013cgroup_root\022\014\n\004ctr" +
            "l\030\001 \001(\t\022\014\n\004path\030\002 \002(\t\"\354\004\n\tcriu_opts\022\025\n\ri" +
            "mages_dir_fd\030\001 \002(\005\022\013\n\003pid\030\002 \001(\005\022\025\n\rleave" +
            "_running\030\003 \001(\010\022\023\n\013ext_unix_sk\030\004 \001(\010\022\027\n\017t" +
            "cp_established\030\005 \001(\010\022\027\n\017evasive_devices\030",
            "\006 \001(\010\022\021\n\tshell_job\030\007 \001(\010\022\022\n\nfile_locks\030\010" +
            " \001(\010\022\024\n\tlog_level\030\t \001(\005:\0012\022\020\n\010log_file\030\n" +
            " \001(\t\022\"\n\002ps\030\013 \001(\0132\026.criu_page_server_info" +
            "\022\026\n\016notify_scripts\030\014 \001(\010\022\014\n\004root\030\r \001(\t\022\022" +
            "\n\nparent_img\030\016 \001(\t\022\021\n\ttrack_mem\030\017 \001(\010\022\022\n" +
            "\nauto_dedup\030\020 \001(\010\022\023\n\013work_dir_fd\030\021 \001(\005\022\022" +
            "\n\nlink_remap\030\022 \001(\010\022\036\n\005veths\030\023 \003(\0132\017.criu" +
            "_veth_pair\022\033\n\007cpu_cap\030\024 \001(\r:\n4294967295\022" +
            "\023\n\013force_irmap\030\025 \001(\010\022\020\n\010exec_cmd\030\026 \003(\t\022\037" +
            "\n\007ext_mnt\030\027 \003(\0132\016.ext_mount_map\022\026\n\016manag",
            "e_cgroups\030\030 \001(\010\022\035\n\007cg_root\030\031 \003(\0132\014.cgrou" +
            "p_root\022\023\n\013rst_sibling\030\032 \001(\010\022\017\n\007app_num\030\033" +
            " \002(\005\"\"\n\016criu_dump_resp\022\020\n\010restored\030\001 \001(\010" +
            "\" \n\021criu_restore_resp\022\013\n\003pid\030\001 \002(\005\"*\n\013cr" +
            "iu_notify\022\016\n\006script\030\001 \001(\t\022\013\n\003pid\030\002 \001(\005\"m" +
            "\n\010criu_req\022\034\n\004type\030\001 \002(\0162\016.criu_req_type" +
            "\022\030\n\004opts\030\002 \001(\0132\n.criu_opts\022\026\n\016notify_suc" +
            "cess\030\003 \001(\010\022\021\n\tkeep_open\030\004 \001(\010\"\322\001\n\tcriu_r" +
            "esp\022\034\n\004type\030\001 \002(\0162\016.criu_req_type\022\017\n\007suc" +
            "cess\030\002 \002(\010\022\035\n\004dump\030\003 \001(\0132\017.criu_dump_res",
            "p\022#\n\007restore\030\004 \001(\0132\022.criu_restore_resp\022\034" +
            "\n\006notify\030\005 \001(\0132\014.criu_notify\022\"\n\002ps\030\006 \001(\013" +
            "2\026.criu_page_server_info\022\020\n\010cr_errno\030\007 \001" +
            "(\005*\214\001\n\rcriu_req_type\022\t\n\005EMPTY\020\000\022\010\n\004DUMP\020" +
            "\001\022\013\n\007RESTORE\020\002\022\t\n\005CHECK\020\003\022\014\n\010PRE_DUMP\020\004\022" +
            "\017\n\013PAGE_SERVER\020\005\022\n\n\006NOTIFY\020\006\022\020\n\014CPUINFO_" +
            "DUMP\020\007\022\021\n\rCPUINFO_CHECK\020\010"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
            new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
              public com.google.protobuf.ExtensionRegistry assignDescriptors(
                      com.google.protobuf.Descriptors.FileDescriptor root) {
                descriptor = root;
                return null;
              }
            };
    com.google.protobuf.Descriptors.FileDescriptor
            .internalBuildGeneratedFileFrom(descriptorData,
                                            new com.google.protobuf.Descriptors.FileDescriptor[]{
                                            }, assigner);
    internal_static_criu_page_server_info_descriptor =
            getDescriptor().getMessageTypes().get(0);
    internal_static_criu_page_server_info_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
            internal_static_criu_page_server_info_descriptor,
            new java.lang.String[]{"Address", "Port", "Pid", "Fd",});
    internal_static_criu_veth_pair_descriptor =
            getDescriptor().getMessageTypes().get(1);
    internal_static_criu_veth_pair_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
            internal_static_criu_veth_pair_descriptor,
            new java.lang.String[]{"IfIn", "IfOut",});
    internal_static_ext_mount_map_descriptor =
            getDescriptor().getMessageTypes().get(2);
    internal_static_ext_mount_map_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
            internal_static_ext_mount_map_descriptor,
            new java.lang.String[]{"Key", "Val",});
    internal_static_cgroup_root_descriptor =
            getDescriptor().getMessageTypes().get(3);
    internal_static_cgroup_root_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
            internal_static_cgroup_root_descriptor,
            new java.lang.String[]{"Ctrl", "Path",});
    internal_static_criu_opts_descriptor =
            getDescriptor().getMessageTypes().get(4);
    internal_static_criu_opts_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
            internal_static_criu_opts_descriptor,
            new java.lang.String[]{"ImagesDirFd", "Pid", "LeaveRunning", "ExtUnixSk", "TcpEstablished",
                                   "EvasiveDevices", "ShellJob", "FileLocks", "LogLevel", "LogFile", "Ps",
                                   "NotifyScripts", "Root", "ParentImg", "TrackMem", "AutoDedup", "WorkDirFd",
                                   "LinkRemap", "Veths", "CpuCap", "ForceIrmap", "ExecCmd", "ExtMnt", "ManageCgroups",
                                   "CgRoot", "RstSibling", "AppNum",});
    internal_static_criu_dump_resp_descriptor =
            getDescriptor().getMessageTypes().get(5);
    internal_static_criu_dump_resp_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
            internal_static_criu_dump_resp_descriptor,
            new java.lang.String[]{"Restored",});
    internal_static_criu_restore_resp_descriptor =
            getDescriptor().getMessageTypes().get(6);
    internal_static_criu_restore_resp_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
            internal_static_criu_restore_resp_descriptor,
            new java.lang.String[]{"Pid",});
    internal_static_criu_notify_descriptor =
            getDescriptor().getMessageTypes().get(7);
    internal_static_criu_notify_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
            internal_static_criu_notify_descriptor,
            new java.lang.String[]{"Script", "Pid",});
    internal_static_criu_req_descriptor =
            getDescriptor().getMessageTypes().get(8);
    internal_static_criu_req_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
            internal_static_criu_req_descriptor,
            new java.lang.String[]{"Type", "Opts", "NotifySuccess", "KeepOpen",});
    internal_static_criu_resp_descriptor =
            getDescriptor().getMessageTypes().get(9);
    internal_static_criu_resp_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
            internal_static_criu_resp_descriptor,
            new java.lang.String[]{"Type", "Success", "Dump", "Restore", "Notify", "Ps", "CrErrno",});
  }

  // @@protoc_insertion_point(outer_class_scope)
}
