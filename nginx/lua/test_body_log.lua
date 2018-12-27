local log_dic = ngx.shared.log_dic

local upstream_time = tonumber(ngx.var.upstream_response_time)

local sum = log_dic:get('upstream_time-sum') or 0
sum = sum + upstream_time
log_dic:set('upstream_time-sum', sum)

local newval, err = log_dic:incr('upstream_time-nb', 1)

if not newval and err == 'not found' then
    log_dic:add('upstream_time-nb', 0)
    log_dic:incr('upstream_time-nb', 1)
end