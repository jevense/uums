count = count + 1
ngx.say('global variable', count)

local share_data = ngx.shared.shared_data
ngx.say('<br>shared variable', share_data:get('count'))
share_data:incr('count', 1)
ngx.say('<br>hello world')