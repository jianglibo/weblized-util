Apache的httpd.conf文件本身相当于一个默认的VirtualHost配置，如果你配置了VirtualHost，那么该VirtualHost的配置会覆盖默认配置。那么没有覆盖的配置呢？就会继承！

最简单的配置：
<VirtualHost *:80>
  ServerName localhost
  ServerAlias localhost
  DocumentRoot "${INSTALL_DIR}/www"
  <Directory "${INSTALL_DIR}/www/">
    Options +Indexes +Includes +FollowSymLinks +MultiViews
    AllowOverride All
    Require local
  </Directory>
<Location "/">
    ProxyPass "http://localhost:8080/"
</Location>
</VirtualHost>