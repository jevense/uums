local gwId = ngx.var.arg_gwId
local vdata = {}
local cjson = require('cjson')

vdata.name = 'mmp'
vdata.version = 'v1.0'
vdata.session = 1
vdata.command = 8
vdata.flow = 0
vdata.sequence = 1

vdata.gwId = gwId
vdata.cfgDate = cfgDate

local jsonRequest = cjson.encode(vdata)

local devCmdKey = 'get_config_request_' .. gwId
local resp = ngx.location.capture('/redis_set?key=' .. devCmdKey, { method = ngx.HTTP_POST, body = jsonRequest })

local responseKey = "get_config_response_" .. gwId

for i = 1, 10, 1 do
    resp = ngx.location.capture('/redis_set?key=' .. responseKey)

    if resp.status == ngx.HTTP_OK and resp.body then
        local parser = require('redis.parser')
        local resp, typ = parser.parse_reply(resp.body)

        resp = ngx.location.capture('/redis_set?key=' .. responseKey)
        if resp ~= nil then
            print(res)
            ngx.exit(200)
        end
    else
        break
    end
    ngx.sleep(1)
end