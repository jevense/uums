if not string.find(ngx.var.request_uri, '.html', 1, true) then
    return
end

local cjson = require('cjson')

local token = ngx.req.get_uri_args()['token']

if token then
    local resp = ngx.location.capture("/validateToken", {
        method = ngx.HTTP_GET,
        args = { token = token }
    })
    if not resp then
        return ngx.say("request error :", err)
    end

    if not resp.status == 200 then
        return ngx.say("status failed: ", resp.status)
    end

    ngx.log(ngx.ERR, "=======", resp.body)

    local body = cjson.decode(resp.body)

    if not body.status then
        return ngx.exit(ngx.HTTP_UNAUTHORIZED)
    end

    ngx.header['Set-Cookie'] = resp.header['Set-Cookie']
else
    local res = ngx.location.capture('/validate')

    if not res then
        return ngx.say("failed to request: ", err)
    end

    if not res.status == 200 then
        return ngx.say("status failed: ", res.status)
    end

    local body = cjson.decode(res.body)
    if not body.status then
        local head = ngx.req.get_headers();
        if 'XMLHttpRequest' == head['X-Requested-With'] or 'application/json' == head['Content-Type'] then
            --如果是ajax请求响应
            return ngx.exit(ngx.HTTP_UNAUTHORIZED)
        else
            local source = 'https://newzp.mvwchina.com/' .. ngx.var.request_uri
            local redirect_uri = '/login?redirect_uri=' .. ngx.escape_uri(source)
            return ngx.redirect(redirect_uri, ngx.HTTP_MOVED_TEMPORARILY)
        end
    end
end

