#!/bin/bash

pip3 uninstall -r requirements.txt

systemctl --user stop mc_hamer.service
systemctl --user disable mc_hamer.service
rm -rf ~/.config/systemd/user
systemctl --user daemon-reload

