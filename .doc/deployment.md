# Deployment

## Cloud Setup

### BW Cloud Instance

1. Register at the BwCloud [https://login.bwidm.de/](https://login.bwidm.de/)
2. Login at [https://portal.bw-cloud.org/project/](https://portal.bw-cloud.org/project/)
   * Create a new volume
   * Create a new instance (__Ubuntu__)
      * Select the new volume
      * Add your ssh `public key`
   * Add new port rules within the security groups
      * Egress TCP `port 80`
      * Ingres TCP `port 80`
3. Login via ssh using the ip like this:

```shell
ssh ubuntu@193.196.52.129
```

### VM Preparation

Update the Server:

```shell
sudo apt-get update
sudo apt-get upgrade
```

Install Docker:

```shell
sudo apt install docker.io
sudo systemctl start docker
sudo systemctl enable docker
sudo groupadd docker
sudo usermod -aG docker ${USER}
```

Pull and run the docker image for the application:

```shell
sudo docker pull ghcr.io/hft-rcpsp-scheduling/rcpsp-data-provider:latest
sudo docker run -d -p 80:8080 --name provider-app ghcr.io/hft-rcpsp-scheduling/rcpsp-data-provider:latest --restart always
```

Check configuration for the program name: `docker-proxy with the Local Address 0.0.0.0:80`

```shell
sudo apt install net-tools
sudo netstat -plnut
```