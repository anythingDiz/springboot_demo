local len = redis.call('llen', KEYS[1])
if (len < tonumber(ARGV[2]))
then
    redis.call('rpush',KEYS[1],ARGV[1])
    return 0
elseif (len > tonumber(ARGV[2]))
then
    redis.call('del',KEYS[1])
    redis.call('rpush', KYES[1], ARGV[1])
    return 0
else
    local start = redis.call('lindex', KEYS[1], 0)
    if((tonumber(ARGV[1]) - tonumber(start)) <= 10000)
    then
        return 1
    else
        redis.call('lpop', KEYS[1])
        redis.call('rpush', KEYS[1], ARGV[1])
        return 0
    end
end