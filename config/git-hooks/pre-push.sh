#!/bin/bash

if [[ $CI == "true" ]]; then
  exit 0
fi

# Check if we have an upstream branch in remote
if [[ -n $(git status -sb | grep 'origin') ]]; then
# Check if we actually have commits to push
  commits=$(git log @{u}..)
  if [[ -z "$commits" ]]; then
    exit 0
  fi
fi

red=$(tput setaf 1)
green=$(tput setaf 2)
reset=$(tput sgr0)
OK="${green}OK${reset}"
CHECK="${green}☑${reset}"
local_branch="$(git rev-parse --abbrev-ref HEAD)"
valid_branch_regex="^(release|hotfix)\/.*$"
echo "0. Check protected branches"
if [[ $local_branch =~ $valid_branch_regex ]]; then
  echo "0. 🛑 Protected branches  "
  echo "***********************************************"
  echo -e "\033[31mInvalid branch name: $local_branch. Please note that release/ and hotfix/ are reserved branch name prefixes. \033[0m"
  echo -e "\033[31mYou must rename your branch to a valid name and try again \033[0m"
  echo "***********************************************"
  exit 1
fi
echo "0. ✅ Check protected branches $OK"
echo " "

# 1. a. Check if we have an upstream branch in remote
echo "1. a. Checking if there is an upstream branch in remote…"
if [[ -n $(git status -sb | grep 'origin') ]]; then
  echo "1. a $CHECK There was an upstream branch in remote $OK"

  # 1. b. Check if there are commits to push
  echo "1. b. Checking if there are commits to push…"
  commits=$(git log @{u}..)
  if [[ -z "$commits" ]]; then
    echo "1. b. 🚨 Check commits to push "
    echo "***********************************************"
    echo "        There are no commits to push       "
    echo "***********************************************"
    exit 0
  fi
  echo "$commits"
  echo "1. b. ✅ Check commits to push $OK"
  echo " "
else
  echo "1. a. ✅ There was NO upstream branch in remote $OK"
  echo " "
fi

# echo SUCCESS
echo "${green}***********${reset}"
echo "${green}*${reset} SUCCESS ${green}*${reset}"
echo "${green}***********${reset}"
exit 0
