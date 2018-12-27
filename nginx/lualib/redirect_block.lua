--print(ngx.var.scheme)
--print(ngx.var.host)
--print(ngx.var.server_name)
--print(ngx.var.server_port)
--print(ngx.var.request_uri)

local redirect_block = function()
    local source = ngx.var.scheme .. '://' .. ngx.var.server_name .. ':' .. ngx.var.server_port .. ngx.var.request_uri
    local redirect_uri = '/login?redirect_uri=' .. ngx.escape_uri(source)
    ngx.redirect(redirect_uri, ngx.HTTP_MOVED_TEMPORARILY)
end

return redirect_block