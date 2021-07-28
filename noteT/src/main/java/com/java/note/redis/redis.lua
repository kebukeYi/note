-- 精简版的 Redis+Lua 实现全局流控的例子
-- 操作的 Redis Key
local rate_limit_key = KEYS[1]
-- 每秒最大的 QPS 许可数
local max_permits = ARGV[1]
-- 此次申请的许可数
local incr_by_count_str = ARGV[2]

-- 当前已用的许可数
local currentStr = redis.call('get', rate_limit_key)
local current = 0
if currentStr then
    current = tonumber(currentStr)
end

-- 剩余可分发的许可数
local remain_permits = tonumber(max_permits) - current
local incr_by_count = tonumber(incr_by_count_str)
-- 如果可分发的许可数小于申请的许可数，只能申请到可分发的许可数
if remain_permits < incr_by_count then
    incr_by_count = remain_permits
end

-- 将此次实际申请的许可数加到 Redis Key 里面
local result = redis.call('incrby', rate_limit_key, incr_by_count)
-- 初次操作 Redis Key 设置 1 秒的过期
if result == incr_by_count then
    redis.call('expire', rate_limit_key, 1)
end

-- 返回实际申请到的许可数
return incr_by_co