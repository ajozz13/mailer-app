#!/bin/bash
#echo "Usage run.sh -t EMAIL_ADDRESS -f FILE_TO_SEND"
exec "/usr/bin/java" -cp .:jars/* mailer $*
