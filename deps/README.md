# LegoSDN & CRIU

LegoSDN uses [Checkpoint and Restore in Userspace (CRIU)](http://criu.org) library for providing support for checkpointing and restoring SDN application state. CRIU, however, is not fully compatible with LegoSDN since the client module (in LegoSDN), which interacts with CRIU to perform checkpoint/restore operations, is written in Java and it cannot communicate with CRIU over a `PF_UNIX/SOCK_SEQPACKET` socket.

## Patch

The file `criu-legosdn-compat.patch` contains changes to the CRIU version `1.3.1` (commit id: `1bbc9fb`) released on September 12, 2014 to make it compatible with LegoSDN.

## Prerequisites

Refer to the the [dependencies](http://criu.org/Installation#Dependencies) listed in the project Website.

## Installation

Pull [CRIU](http://criu.org/)'s source code from Github and switch to the branch contaiing the stable version `1.3.1`. Then apply the patch to make the version compatible with LegoSDN.

```
$ git clone https://github.com/xemul/criu
$ git co remotes/origin/br-1.3-stable
$ git apply --check criu-legosdn-compat.patch
# The above command should produce no output.
$ git apply --stat criu-legosdn-compat.patch
 cr-service.c                |    2
 cr-service.c                |  390 ++++++++++++++++++++++++++++++++++++-
 crtools.c                   |    9 +
 include/cr-service-const.h  |    4
 include/cr-service.h        |    2
 cr-service.c                |  362 +++++++++++++++--------------------
 crtools.c                   |    8 -
 image.c                     |   13 +
 include/cr-service.h        |   17 ++
 launch-criu-tcp-service.sh  |   32 +++
 cr-service.c                |    2
 cr-service.c                |  451 ++++++++++++++++++++++++++++---------------
 include/cr-service.h        |   11 +
 launch-criu-tcp-service.sh  |   30 ++-
 protobuf/rpc.proto          |    1
 protobuf/rpc.proto.original |  108 ++++++++++
 cr-service.c                |    2
 17 files changed, 1060 insertions(+), 384 deletions(-)
```

At this point, the source code is ready to be compiled. Follow the instructions to [build CRIU from source](http://criu.org/Installation#Building_CRIU_From_Source) provided at the project's Website.

## Running

Prior to running the modified CRIU service, please run the script `${LEGOSDN_HOME}/ext/gen-rt-deps.sh`. You can now launch the service using the script `launch-criu-tcp-service.sh`.

```
~/legosdn/criu$ ./launch-criu-tcp-service.sh
> removed old checkpoints in '/legosdn/checkpoints'

```
