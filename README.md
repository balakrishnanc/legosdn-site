# LegoSDN

[LegoSDN](http://legosdn.cs.duke.edu) is a re-design of the SDN controller architecture centering around a set of abstractions to eliminate the fate-sharing relationships (between the SDN-Apps and the controller, and between the SDN-Apps and the network), and make the controller and network resilient to SDN-App failures.

You can learn more about the project at [http://legosdn.cs.duke.edu](http://legosdn.cs.duke.edu)


## Prerequisites

1. Oracle Java JDK/JRE (>=1.6)
2. Git
3. Apache ant
4. [Floodlight](http://www.projectfloodlight.org/floodlight/)
5. [CRIU](http://www.criu.org)


## Getting the source code

Start by retrieving the source code of Floodlight and patching the build file of the project by following the instructions in `deps/dep-floodlight/README.md`. Install CRIU by following the instructions in `deps/dep-criu/README.md`. After installing Floodlight and CRIU, clone the LegoSDN repository on your machine.

```
$ git clone https://github.com/balakrishnanc/legosdn.git
```

## Building

Ensure that the following environment variables are set.

```
export JAVA_HOME="/path/to/JDK-or-JRE"
export PATH=${JAVA_HOME}/bin:${PATH}
export FLOODLIGHT_HOME="/path/to/Floodlight"
export CRIU_HOME="/path/to/CRIU"
export LEGOSDN_HOME="/path/to/LegoSDN"
export LEGOSDN_RT="/legosdn/runtime"
```

The path indicated by the environment variable `LEGOSDN_RT` will be required for storing runtime data. This path can be created runtime setup scripts described in the next section. If you are using BASH shell, the template file `conf/template.bash_profile` can be used to create a `.bash_profile` to set the environment properly on login.

```
$ cp -v conf/template.bash_profile ~/.bash_profile
# Edit the profile file as required.
```

You can build both Floodlight and LegoSDN using the scripts `build.sh` or `clean-build.sh`. The latter deletes any current build artifacts and rebuilds the source from scratch. Do not attempt the build it manually using ant and `build.xml` since there are circular dependencies.

```
$ cd ${LEGOSDN_HOME}
$ ./clean-build.sh
```


## Configuration

### Runtime Configuration

LegoSDN requires the mountpoint `/legosdn` to be setup, and the script `tools/setup-mt-pts.sh` needs to be run (as root) to setup this mount point.

```
$ cd ${LEGOSDN_HOME}
$ sudo ./tools/setup-mt-pts.sh
```

Prior to running any of LegoSDN's components, please set the `LEGOSDN_RT` environment variable to point to a location where all runtime configuration data will be stored. The runtime location should ideally reside in a custom mount point at `/legosdn`. Please, take a look at the bash shell configuration template provided in `conf/template.bash_profile`. Note that the runtime path need not exist physically on disk or memory at this point. This path will be generated and populated with configuration data, once you run script `tools/gen-rt-deps.sh`. The script generates the required runtime directories and copies the configuration files.

```
$ cd ${LEGOSDN_HOME}
$ ./tools/gen-rt-deps.sh
```

The following set of directories should exist inside of `/legosdn` at this point.

```
/legosdn/
├── checkpoints
└── runtime
    ├── app
    │   ├── logs
    │   └── resources   (files loaded by application at runtime)
    ├── conf
    ├── counters
    ├── crash-flags
    ├── crash-indicators
    ├── stubs
    ├── timers
    └── tools
```

The following shows a sample listing of runtime configuration files; not all of these files may exist on your machine, at this point.

```
/legosdn/runtime/conf/
├── ctrlr-appv.properties
├── edu.duke.cs.legosdn.tests.apps.hub.Hub.properties
├── edu.duke.cs.legosdn.tests.apps.rf.RouteFlow.properties
└── logback.xml
```

The file `ctrlr-appv.properties` contains settings to configure the behavior of the controller and AppVisor. The file `logback.xml` is used for configuring the level of logging in all components — Controller, AppVisor,  AppLoader, and Applications.

Property files to customize the behavior of different applications are named after the canonical name of the application launcher class. For instance, the properties file for the application launcher class `edu.duke.cs.legosdn.tests.apps.hub.Hub` is named `edu.duke.cs.legosdn.tests.apps.hub.Hub.properties`. Note that the canonical name of the launcher is required to also launch the application from the command-line.

### Floodlight Configuration

Floodlight also requires that the modules to be loaded by the controller be specified upfront, and hence, the Floodlight modules file should contain at least the following two entries.

```
edu.duke.cs.legosdn.core.appvisor.proxy.AppProxy
edu.duke.cs.legosdn.core.appvisor.proxy.AppAwareLinkDiscoveryManager
```

The `AppProxy` class allows the applications to be started in isolated containers, and the `AppAwareLinkDiscoveryManager` class maintains per-app state related to links discovered in the network. The latter also requires that you disable the internal (bundled with Floodlight) `LinkDiscoveryManager` module by commenting it out (in the modules file). Modify the file further depending on what other modules or applications you intend to load.

### AppVisor Configuration

The sample properties file `ctrlr-appv.properties` to configure the controller and AppVisor is provided below.

```
floodlight.modules=edu.duke.cs.legosdn.core.appvisor.proxy.AppProxy,\
  net.floodlightcontroller.topology.TopologyManager,\
  net.floodlightcontroller.counter.NullCounterStore,\
  net.floodlightcontroller.perfmon.NullPktInProcessingTime
net.floodlightcontroller.restserver.RestApiServer.port = 8080
net.floodlightcontroller.core.internal.FloodlightProvider.openflowport = 6633
net.floodlightcontroller.forwarding.Forwarding.idletimeout = 5
net.floodlightcontroller.forwarding.Forwarding.hardtimeout = 0
edu.duke.cs.legosdn.core.appvisor.proxy.AppProxy.chkpt_freq = 0
edu.duke.cs.legosdn.core.appvisor.proxy.AppProxy.disable_netlog = true
edu.duke.cs.legosdn.core.appvisor.proxy.AppProxy.transformer = edu.duke.cs.legosdn.core.faults.BasicTransformer
edu.duke.cs.legosdn.core.appvisor.proxy.AppProxy.disable_xforms = false
edu.duke.cs.legosdn.core.appvisor.proxy.AppProxy.use_local_netlog = false
edu.duke.cs.legosdn.core.appvisor.proxy.AppProxy.invert_xforms = false
edu.duke.cs.legosdn.core.appvisor.proxy.AppProxy.disable_per_app_ns=false
edu.duke.cs.legosdn.core.appvisor.proxy.AppProxy.halt_on_cr_fail = true
edu.duke.cs.legosdn.core.appvisor.proxy.AppProxy.enable_msglog = false
```

The configuration parameter `floodlight.modules` lists the applications that should be loaded along with the controller instance. The value provided in the sample file should suffice; do not modify this value unless you know exactly what you are doing.

The rest of the lines follow a simple pattern where the configuration parameter is named after the class that expects the parameter. The word after the last period of each such configuration parameter indicates that actual parameter that a particular class expects. For instance, the class AppProxy, which is one component of the AppVisor module of LegoSDN, expects a value for the configuration parameter 'checkpt_freq' to decide how frequently to checkpoint the SDN applications. The configuration parameter, hence, is named `edu.duke.cs.legosdn.core.appvisor.proxy.AppProxy.chkpt_freq` which is simply the class name followed by a period followed by the actual configuration parameter name expected by the class.

The following are the configuration parameters to customize the behavior of the AppVisor.

| Parameter               | Value 
| -----------------------:|:------
| *chkpt\_freq*           | Rate (in terms of input messages to App) at which an application should be checkpointed. Setting this to zero, disables checkpointing. *Default: None.*
| *disable\_netlog*       | Setting it to true disables NetLog. *Default: false.*
| *transformer*           | Fully qualified name of a class implementing event transformations. *Default: edu.duke.cs.legosdn.core.faults.BasicTransformer.*
| *disable\_xforms*       | Setting it to true disables event transforms. *Default: false.*
| *use\_local\_netlog*    | Run NetLog within the controller. *Default: true.*
| *invert\_xforms*        | If set to true, it allows event transformer to invert prior transforms to allow an application to process an input message without issues. *Default: false.*
| *disable\_per\_app\_ns* | Setting it to true disables per-app network state management. *Default: false.*
| *halt\_on\_cr\_fail*    | Set it to true to halt controller on failures during checkpoint or restore operations. *Default: true.*
| *enable\_msglog*        | Set it to true to write inbound and outbound messages to file for debugging. *Default: false.*

#### Application Configuration

A couple of sample application properties file are provided in the `conf` directory. Copy the files to appropriate path and modify the properties as required by your application.  Shown below is the sample properties file for the `RouteFlow` application, and brief notes on the meaning of the different parameters.

```
app.name=edu.duke.cs.legosdn.tests.apps.rf.RouteFlow
app.port=9901
app.stateless=false
edu.duke.cs.legosdn.tests.apps.rf.RouteFlow.enable_msglog=false
edu.duke.cs.legosdn.tests.apps.rf.RouteFlow.host_to_sw_mappings=/legosdn/runtime/app/resources/edu.duke.cs.legosdn.tests.apps.rf.RouteFlow.HostToSwMappings.txt
# fault.injector.class=edu.duke.cs.legosdn.core.faults.NthPacketFaultInjector
# edu.duke.cs.legosdn.core.faults.NthPacketFaultInjector.msgsbetweenfaults=50
```

The configuration parameters starting with `app.` are LegoSDN related. The first two identify the (main) application class and the port over which the controller can send data to the application for processing. The third parameter is optional and helps LegoSDN decide how to recover the application in the event of a crash. By default, applications are assumed to be stateless.

Configuration parameters started with the canonical name of the application, `edu.duke.cs.legosdn.tests.apps.rf.RouteFlow` in this example, are parameters that the application is designed to accept. This is similar to how application specific configuration is passed via a properties when deploying the application over Floodlight; this makes porting an existing Floodlight application to LegoSDN much easier. The last two show configuration related to fault injectors--modules that generate faults to crash the application for testing.

### Running

To run Floodlight with LegoSDN components start the following components in the order specified: CRIU, Controller with AppVisor, AppLoader.

```
# Start CRIU
$ cd ${CRIU_HOME}
$ ./launch-criu-tcp-service.sh

# Start the controller with AppVisor
$ cd ${FLOODLIGHT_HOME}
$ ${LEGOSDN_HOME}/run-ctrlr-appv-lomem.sh

# Start the AppLoader using the canonical name of the app-launcher class
$ cd ${LEGOSDN_HOME}
# Syntax: ./apploader-lomem.sh <class-name>
$ ./apploader-lomem.sh edu.duke.cs.legosdn.tests.apps.rf.RouteFlow
```

The JVMs (one each for NetLog, Floodlight and LegoSDN) require huge heaps and hence, running inside a VM can cause performance problems. The *-lomem.sh suffixed scripts basically launch each JVM with a minimal heap configuration. If your application requires more heap, adjust the heap settings accordingly.
