# dbBrowser
This is a simple CRUD application over mysql.
 
# Getting Started
Follow the instruction below to use application.
1) Install xampp, wampserver,also you may use docker. I have docker-compose file under externals folder.
2) Create a database named dbBrowser, for docker users when you run the docker-compose file it will create the database itself
3) Populate the database with the data.sql file, just import it via phpmyadmin web application. For dcoker users you may copy and paste the following command that I give.
4) Run the app
 - take backup
 - docker exec [containerName] /usr/bin/mysql dump -u [userName] --password=[dBpassword] [databaseName] > [fileName].sql

 - restore commamand
 - cat [fileName].sql | docker exec -i [containerName] /usr/bin/mysql -u [userName] --password=[dBpassword] [databaseName]

 - The configuration that I gave inside docker-compose.yml file:
 - username: root
 - dbPassword: 1234
  
## Authors
- [Ersin ÇEBİ](https://github.com/ersincebi)
- [Tolga Güldütuna](https://github.com/TolgaGuldu)
    
## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details


