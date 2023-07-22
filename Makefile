db-status:
	java -jar target/user-service-1.0-SNAPSHOT.jar db status config.yml

db-tag:
	java -jar target/user-service-1.0-SNAPSHOT.jar db tag config.yml $(LOGPATH)$(shell date +'%y.%m.%d')

db-migrate:
	java -jar target/user-service-1.0-SNAPSHOT.jar db migrate config.yml

db-rollback:
	java -jar target/user-service-1.0-SNAPSHOT.jar db rollback config.yml --tag $(LOGPATH)$(shell date +'%y.%m.%d')

tt:
	echo $(LOGPATH)$(shell date +'%y.%m.%d')


