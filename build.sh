#!/bin/bash
repository=dkerhater/jpa-rest
tagName=dev

read -p "배포할 버전을 입력하세요:" version

echo "=>>> Gradle 빌드 중입니다...."
./gradlew clean build

echo "=>>> 도커 이미지를 생성 중입니다...."
docker build -f Dockerfile.dev -t $repository:$version-$tagName .

echo "=>>> 도커 레포지토리로 이미지를 전송 중입니다...."
docker push $repository:$version-$tagName

echo "🚀 $repository:$version-$tagName 도커 레포지토리에 이미지 전송을 완료했습니다."