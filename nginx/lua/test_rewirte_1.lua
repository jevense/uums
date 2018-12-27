if ngx.req.get_uri_args()['jump'] == "1" then
    return ngx.redirect('http://www.baidu.com', 302)
end