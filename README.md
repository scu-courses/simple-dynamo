# Simple Dynamo
Simple Dynamo is a distributed key-value store that includes a subset of features from the [Amazon Dynamo](https://www.allthingsdistributed.com/files/amazon-dynamo-sosp2007.pdf) paper that achieves ***scalability*** and ***availability*** with a compromise on ***consistency (eventual consistency)*** 

## Design
Main design choices of the Simple Dynamo are listed below:

* Architecture: a ring architecture to organize and partition all nodes and keys
* System interface: only support `put` and `get` function through client library
* Request routing: each node should be able to serve or route client request, the node is chosen in a round-robin fashion
* Data partitioning: every node is responsible for keys within the range of `[node token, next node token)`
* Data replication: each data is replicated at N = 3 nodes
* Consistency: we use quorum protocol which set R = 2, W = 2 (R + W > N) to resolve inconsistencies.
* Failure Handling: in our system setup, we can tolerate 1 node in the quorum to fail and still get a valid response (if there's no conflict)
* A ring architecture to organize and partition all nodes and keys
* Data is partitioned and replicated using consistent hashing
* Gossip-based membership protocol and failure detection
* Adding / Removing nodes dynamically, rebalancing keys after membership change
* a client library (load balancer) for routing requests to functioning nodes in a round-robin fashion

## Setup / Deployment / Test guide

### System environments
