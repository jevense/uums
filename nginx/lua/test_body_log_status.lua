local log_dic = ngx.shared.log_dic

local sum = log_dic:get('upstream_time-sum')
local nb = log_dic:get('upstream_time-nb')

if nb and sum then
    ngx.say('average upsteam response time ', sum / nb, '(', nb, ' reqs)')
else
    ngx.say('no data yet')
end