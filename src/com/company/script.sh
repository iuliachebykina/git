for user in $(git log --pretty="%ce%n" | sort | uniq);
do
   # print user, total insertions + total deletions
   echo "$user" $(git log --author="${user}" --shortstat | awk '/^ [0-9]/ { i += $4 + $6 } END { printf("%d", i) }')
done