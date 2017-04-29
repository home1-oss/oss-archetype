# OSS-ARCHETYPE-MICRO-SERVICE-CONFIG

## 简介

本项目是“微服务配置repository”的骨架，用于生成可以在git仓库中进行版本控制的配置项目，供配置中心进行统一管理。  

基于本项目骨架生成的配置项目中包含如下内容:
  - yml的配置文件;
  - 配置文件格式检查工具;
  - git hooks工具pre-commit;
  - gitlab-ci 脚本及配置。

## 使用
### 创建项目

开发人员需要通过`mvn archetype:generate`命令，基于本骨架项目创建新的配置管理项目。示例如下：

    ARCHETYPE_VERSION="1.0.6.OSS-SNAPSHOT"
    #ARCHETYPE_REPOSITORY="${INTERNAL_NEXUS}/content/groups/public"
    ARCHETYPE_REPOSITORY="${LOCAL_NEXUS}/repository/maven-public"

    groupId="configserver"
    artifactId="micro-service-config"
    package="cn.home1"
    version="0.0.1-SNAPSHOT"

    mvn archetype:generate \
        -DarchetypeGroupId=cn.home1 \
        -DarchetypeArtifactId=oss-archetype-micro-service-config \
        -DgroupId=${groupId} \
        -DartifactId=${artifactId} \
        -Dversion=${version} \
        -Dpackage=${package} \
        -DarchetypeVersion=${ARCHETYPE_VERSION} \
        -DarchetypeRepository=${ARCHETYPE_REPOSITORY}

    cd ${artifactId}
    mvn clean package

## 项目组成说明

项目创建之后，目录及文件组织结构如下所示：

    .
    ├── application.yml
    ├── gitlab-ci.sh
    ├── hooks
    │   ├── pre-commit
    │   └── oss-configlint.jar
    ├── pom.xml
    `── README.md

下面针对各文件及作用进行说明。

### YAML配置文件
默认创建的配置仓库中，会有一个application.yml的配置文件，这也是我们建议的配置文件格式。

> + 用户可以使用properties作为配置文件的格式，**强烈建议使用yml，不要使用properties**。  
> + 如果一定要使用properties格式的配置文件，用户需要修改git hooks命令以及pom文件，以便在git提交以及项目ci的时候进行文件格式检查。

### 配置文件格式校验工具
hooks目录下的`oss-configlint.jar`可以用于yml、properties格式的配置文件检查，用于检查配置文件格式的合法性以及key冲突，使用方式： 

    # 多个文件用空格分隔
    java -jar hooks/oss-configlint.jar file1.yml [ file2.yml ...] 

> 文件格式校验已经配置到git的pre-commit脚本以及gitlab-ci，git hooks需要用户在本地手动配置。

### git hooks 配置
**git hooks简介** 

git hooks是一些在`.git/hooks`目录的脚本，在被特定的事件(certain points)触发时被调用。当git init命令被调用后，一些常用的示例钩子文件被拷贝到新仓库的hooks目录中；但是默认这些钩子时不生效的。把.sample后缀去掉之后生效。

**对git hooks的支持**

使用oss项目骨架生成的配置文件项目，会在项目根目录建立`hooks`目录，将配置文件格式检查的命令放到`pre-commit`脚本。这样在项目本地commit的时候会自动检查`application.yml`文件的合法性以及key冲突情况，**如果文件内容非法，本地提交会失败**。

+ 若要是git hooks生效，用户需要在项目第一次创建或者clone下来之后，修改git hooks的目录设置。**具体配置请查看项目中的配置文件**。  
+ 用户如果有新增配置文件***或者***使用properties配置文件的情况，需要修改`hooks/pre-commit`脚本；

### gitlab-ci 脚本及配置

在生成的项目中，通过maven插件，将配置文件的格式校验过程放到maven的生命周期里，用户可以通过`mvn clean package`命令 ，执行格式校验工作。同时，将格式校验加入到gitlab-ci过程，开发人员将配置文件提交到git仓库之后会再执行一次校验工作。

**attention**  

用户如果有新增配置文件或者使用properties配置文件的情况，除了需要修改`hooks/pre-commit`脚本之外，还需要修改`pom.xml`里的maven配置，如下所示：

    <configuration>
        <executable>${java.home}/bin/java</executable>
        <workingDirectory>${user.dir}</workingDirectory>
        <arguments>
            <argument>-jar</argument>
            <argument>${project.basedir}/hooks/oss-configlint.jar</argument>
            <argument>application.yml</argument>
        </arguments>
    </configuration>

修改`<argument>application.yml</argument>`这一行，如果有多个配置文件，在下面继续追加`<argument>`参数即可

## 建议
我们强烈建议开发人员按照项目骨架指定的方式来管理应用的配置，除了可以享受yml带来的便利，也可以不用对项目做任何修改就能够享受到自动配置文件校验的好处。
