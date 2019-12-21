# dbBrowser

 - docker exec [containerName] /usr/bin/mysql dump -u [userName] --password=[dBpassword] [databaseName] > [fileName].sql
 - cat [fileName].sql | docker exec -i [containerName] /usr/bin/mysql -u [userName] --password=[dBpassword] [databaseName]