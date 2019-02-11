--
-- Created by IntelliJ IDEA.
-- User: T011689
-- Date: 2018/12/27
-- Time: 8:51
-- To change this template use File | Settings | File Templates.
--
local count=tonumber(ARGV[1]);
redis.log(redis.LOG_WARNING,count);
local key='stock:'..KEYS[1];
local flag=redis.call('get',key);
redis.log(redis.LOG_WARNING,flag);
if flag then
    if tonumber(flag)>=count then
--        减库存失败;
        return redis.call('decrby',key,count);
    else
        return -3
    end
else
--    没有这个key
    return -2;
end

