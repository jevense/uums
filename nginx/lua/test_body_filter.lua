local chunk = ngx.arg[1]
if string.match(chunk, 'hello') then
    ngx.arg[2] = true --new eof
    return
end

ngx.arg[1] = nil