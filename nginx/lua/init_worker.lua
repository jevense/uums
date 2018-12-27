---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by lujiewen.
--- DateTime: 2018/12/23 上午11:48
---

local count = 0
local delayInSeconds = 3
local heartbeatCheck = nil

heartbeatCheck = function()
    count = count + 1
    ngx.log(ngx.ERR, 'do check', count)

    local ok, err = ngx.timer.at(delayInSeconds, heartbeatCheck)

    if not ok then
        ngx.log(ngx.ERR, 'failed to startup heartbeart worker...', err)
    end

end

heartbeatCheck()