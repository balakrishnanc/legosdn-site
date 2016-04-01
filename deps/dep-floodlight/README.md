# LegoSDN & FloodLight

LegoSDN's AppVisor component runs similar to a regular SDN application on top of Floodlight. Floodlight requires that the SDN applications be bundled along with the controller in a single JAR file. Since LegoSDN's components require a build of Floodlight to compile, there is a circular dependency in the build. 

## Prerequisites

Refer to the the [prerequisites](http://www.projectfloodlight.org/getting-started/) listed in the project Website.

## Building

After obtaining the source code for Floodlight, follow the [build instructions](http://www.projectfloodlight.org/getting-started/) in the project Website to generate a floodlight JAR file. Rename this JAR file as follows.

```
$ git clone git://github.com/floodlight/floodlight.git
$ cd floodlight
$ ant
# Follow any additional steps that are listed on the project Website.
$ mkdir -p dist
$ cp -v target/floodlight.jar dist/floodlight.core.jar
```

## Integration

To integrate Floodlight with LegoSDN, modify the `build.xml` as follows.

Add the following lines prior to target definitions in the build file.
```
<!-- ... ... ... -->
<project default="dist" name="floodlight">
<!-- ... existing configuration ... -->

<property environment="env"/>
<property name="legosdn.home" value="${env.LEGOSDN_HOME}"/>

<patternset id="legosdn-lib">
    <include name="legosdn-core.jar"/>
    <include name="legosdn-tools.jar"/>
    <include name="legosdn-tests.jar"/>
</patternset>

<patternset id="legosdn-third-party-libs">
    <include name="fst-2.01.jar"/>
    <include name="protobuf-java-2.6.0.jar"/>
</patternset>

<target name="compile" depends="init">
<!-- ... existing configuration ... -->
```

Modify the `dist` target to include the following additional configuration lines in the `jar` task.

```
<target name="dist" depends="compile">
    <jar destfile="${floodlight-jar}" filesetmanifest="mergewithoutmain">
        <!-- ... existing configuration ... -->
        <zipgroupfileset dir="${legosdn.home}/dist">
            <patternset refid="legosdn-lib"/>
        </zipgroupfileset>
        <zipgroupfileset dir="${legosdn.home}/third-party">
            <patternset refid="legosdn-third-party-libs"/>
        </zipgroupfileset>
    </jar>
</target>
```
