local balancer = require('ngx.balancer')
local port = { 8088, 8089 }
local david = ngx.req.get_uri_args()['david'] or 0
local hash = (david % 2) + 1
local backend = port[hash]
ngx.log(ngx.ERR, 'david=', david, ' hash=', hash, ' up=', backend)

local ok, err = balancer.set_current_peer('127.0.0.1', backend)

if not ok then
    ngx.log(ngx.ERR, 'failed to set the current peer : ', err)
end

ngx.log(ngx.DEBUG, ' current peer : ', backend)