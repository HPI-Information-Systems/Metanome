#!/bin/sh

echo "Adding Pre-Commit Hook for LICENSE-MAINTAINER!"

ln -s -f ../../.githooks/license-maintainer/pre-commit ../.git/hooks/pre-commit

if [ $? -eq 0 ]; then
    echo OK
else
    echo FAIL
fi
