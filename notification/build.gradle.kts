dependencies {
    implementation(project(":common"))
    compileOnly(project(":core:user-core"))
    compileOnly(project(":core:todo-core"))
    compileOnly(project(":chat"))

    implementation("com.google.firebase:firebase-admin:9.8.0")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.springframework.boot:spring-boot-starter")
}
