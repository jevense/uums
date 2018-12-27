local redis = require('resty.redis')
local red = redis:new()

red:set_timeout(1000) -- 1 sec

--local ok,err = red:connect('unix://path/to/redis.sock')
local ok, err = red:connect('127.0.0.1', 6379)
if not ok then
    ngx.say('failed to connect : ', err)
    return
end

ok, err = red:set('dog', 'an animal')
if not ok then
    ngx.say('failed to set dog : ', err)
    return
end

ngx.say('set result : ', ok, '<br>')

local res, err = red:get('dog')

if not res then
    ngx.say('failed to get dog : ', err)
    return
end

if res == ngx.null then
    ngx.say('dog not found ')
    return
end

ngx.say('dog : ', res, '<br>')

red:init_pipeline()

red:set('cat', 'marry')
red:set('horse', 'bob')
red:get('cat')
red:get('horse')

local results, err = red:commit_pipeline()

if not results then
    ngx.say('filed to commit th pipelined requests:', err)
    return
end

for i, v in ipairs(results) do
    if type(res) == 'table' then
        if res[1] == false then
            ngx.say('failed to run command', i, ':', res[2])
        else
            --process th table value
        end
    else
        -- process the scalar value
    end
end

ok, err = red:set_keepalive(10000, 100)
if not ok then
    ngx.say('failed to set keepalive: ', err)
    return
end