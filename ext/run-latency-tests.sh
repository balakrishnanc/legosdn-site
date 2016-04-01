#!/usr/bin/env bash

# [ CONFIGURATION SECTION ]
# ------------------------------
# Modify parameter values to vary test conditions.

# Controller Hostname
CONTROLLER_HOST=galvatron.cs.duke.edu

# Effective number of test loops
# NOTE: Interpret this as the number of data points based on which
# performance statistics are to be collected.
EFF_NUM_LOOPS=30

# Max number of switches to test with
MAX_NUM_SW=16

# Total number of MACs to use for each test
NUM_MACS=10000

#
# END OF CONFIGURATION SECTION
# ------------------------------


# Number of warmup loops
NUM_WARMUP_LOOPS=2

# Total number of test loops
let NUM_LOOPS=${EFF_NUM_LOOPS}+${NUM_WARMUP_LOOPS}

echo "#loops-per-test: ${NUM_LOOPS}" >&2

n=${EFF_NUM_LOOPS}
test_id=0
total_tests=`echo "1+${MAX_NUM_SW}/2" | bc`
echo '#sw #tests min max avg stdev'
for s in $(echo 1 "$(seq 2 2 ${MAX_NUM_SW})"); do
    let test_id="${test_id}+1"
    echo "> testing configuration ${test_id} of ${total_tests}" >&2

	cbench                                                                  \
	-c ${CONTROLLER_HOST}                                                   \
	-l ${NUM_LOOPS}                                                         \
	-w ${NUM_WARMUP_LOOPS}                                                  \
	-s ${s}                                                                 \
	-m 1000                                                                 \
	-M ${NUM_MACS}                                                        |	\
	grep RESULT                                                           |	\
	awk -v n=${n} -v sw=${s} '{gsub("/", ",", $8); print sw","n","$8}'    |	\
	sed 's/,/ /g'
done
