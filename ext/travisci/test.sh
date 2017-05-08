#!/bin/bash

set -e

# Just ensure there are no dependency conflicts
lein deps :tree
