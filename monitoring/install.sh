#!/bin/bash

user_name=$USER
destination=/home/"$user_name"/.config/systemd/user/
mkdir -p "$destination"
u_flag=false #for required arg url
t_flag=false #for required arg token
mc="mc_hamer.py"

# Usage info
show_help() {
    cat << EOF
Usage: ${0##*/} [-hutni] [VALUE]...
This script will install the MC fully automated:
-> install all python3 requirements
-> create a systemd user target file for running as service
-> enable service
-> if wanted, start service
    -h          display this help and exit
    -u          url of the backend server (required)
    -t          the super secret token to register (required)
    -n          name of the node (optinal)
    -i          sending interval (optinal)
EOF
}

while getopts "hu:t:n:i:" opt; do
    case $opt in
        h)
            show_help
            exit 0
            ;;
        u)  url=$OPTARG
            regex='^[-A-Za-z0-9\+&@#/%?=~_|!:,.;]*[-A-Za-z0-9\+&@#/%=~_|]'
            if [[ !( $url =~ $regex ) ]] ; then
                echo "[-] your url is not valid."
                echo "[-] for example: http[s]://example:7331"
                exit 1
            else
                u_flag=true
            fi
            ;;
        t)  token=$OPTARG
                t_flag=true
            ;;
        n)  name=$OPTARG
            ;;
        i)  interval=$OPTARG
            re='^[0-9]+$'
            if ! [[ $interval =~ $re ]] ; then
                echo "[-] the interval needs to be a number" >&2;
                exit 1
            fi
            ;;
        *)
            show_help >&2
            exit 1
            ;;
    esac
done

#in case interval is not set yet
if [[ -z "$interval" ]]; then
    interval=0
fi

#check if url and token are provided
if [[ ( "$u_flag" = false || "$t_flag" = false ) ]] ; then
    echo "[-] the options '-u' and '-t' are required options! For more infos plz use '-h'"
    exit 1
fi

#insert parameter in MC script
sed -i "s/^TOKEN=.*/TOKEN=\'$token\'/" "$mc"
sed -i "s,^REG_URL=.*,REG_URL=\'https://$url/api/nodes\'," "$mc"
sed -i "s,^URL=.*,URL=\'https://$url/api/monitoring/records\'," "$mc"
sed -i "s,^CONF_URL=.*,CONF_URL=\'https://$url/api/nodes/configuration\'," "$mc"
sed -i "s/^NAME=.*/NAME=\'$name\'/" "$mc"
sed -i "s/^INTERVAL=.*/INTERVAL=$interval/" "$mc"
sed -i "s,^CA_PATH=.*,CA_PATH=\'$(pwd)/ca.crt\'," "$mc"

pip3 install --user -r requirements.txt

# create systemd unit for user
cat << EOF > "$destination"/mc_hamer.service
[Unit]
Description=Monitoring Client for HAM EMS

[Service]
ExecStart=/usr/bin/python3 $(pwd)/$mc
Restart=always

[Install]
WantedBy=default.target
EOF

#allow user service to run after logout
loginctl enable-linger 

#enable service
systemctl --user enable mc_hamer

#run HAM MC?
read -p "Start HAM MC? (Y/N): " confirm && [[ $confirm == [yY] || $confirm == [yY][eE][sS] ]] || exit 1
systemctl --user start mc_hamer
systemctl --user status mc_hamer
