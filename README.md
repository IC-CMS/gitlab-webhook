# gitlab-webhook

gitlab-webhook implements the webhook for version 7 - 9 of Gitlab
Configuration:
Expected configuration file located at /data/gitlab-webhook/configuration/application.properties.  This file must have some
or all of the following properties specified

Mongo Settings

SSL Settings -- These settings are for the connection to mongo only.
mongodb.keyStoreLocation -- The location of the java keystore.  This is used for 2-way SSL enabled connection to mongo only.
mongodb.keyStoreKeyPassword -- The password of the KEY stored a java keystore.  This may be the same as the keystore password or different.
mongodb.keyStorePassword -- The password of the java KEYSTORE.
mongodb.trustStoreLocation -- The location of the truststore
mongodb.trustStorePassword

Location Settings -- These settings help find mongo.
mongodb.replicaSetLocation -- The location(s) (DNS or IP address) of mongo's replica-set.
mongodb.databaseName -- The name of the mongo database.
mongodb.mongoReplicaSetName -- The name of mongo's replica-set.

Mongo Credential Settings -- These settings determine the identity to use to connect to mongo.
mongodb.username -- The username of the identity
mongodb.password -- The password of the identity.

Gitlab Settings

gitlab.classification -- The classification of the data incoming from Gitlab.

