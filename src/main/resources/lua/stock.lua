--
-- Created by IntelliJ IDEA.
-- User: T011689
-- Date: 2018/12/27
-- Time: 8:51
-- To change this template use File | Settings | File Templates.
--
local count=tonumber(ARGV[1])
local key='stock:'..KEYS[1]
local flag=redis.call('get',key)
if flag then
    if tonumber(flag)>=count then
        return redis.call('decrby',key,count)
    else
--        adadsad
        return -3
    end
else
    return -2
end

