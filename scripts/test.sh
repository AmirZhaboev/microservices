for i in {1..3}; do
  http GET :8080/v1/organization/lol/license/67572af2-2d79-4e06-ba2d-e898fed9c89b > /dev/null &
done
wait