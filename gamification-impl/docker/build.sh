cp  ../target/gamification-impl-1.0.0.jar  ./artifact/gamification-impl.jar
chmod 777 ./artifact/gamification-impl.jar
docker build -t spring/gamification .