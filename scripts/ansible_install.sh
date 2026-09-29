sudo apt update
sudo apt install python3-pip python3-venv -y
python3 -m venv ansible-env
source ansible-env/bin/activate
pip install --upgrade pip
pip install ansible
which ansible
ansible --version

