if redis.call('setnx', KEYS[1], ARGV[1]) == 1 then
    return redis.call('pexpire', KEYS[1], ARGV[2])
else
    return 0
end