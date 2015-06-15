#	docker

		docker run -it -p 8080:80  -v /home/docker:/home/dockers daocloud.io/shaalx/echo:master-3f9a7a5

		docker commit 1694a3eca48fe668e30ed328dafaf31b8f7346675185a5daf4b3ee574db4d2ee shaalx:goecho

		 docker build -t shaalx .