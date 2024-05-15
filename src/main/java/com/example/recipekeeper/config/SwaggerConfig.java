/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.recipekeeper.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(buildInfo());
    }

    private Info buildInfo() {
        return new Info()
                .title("Recipe Keeper API Documentation")
                .version("1.0")
                .description("REST API documentation of the Recipe Keeper")
                .license(buildLicense())
                .contact(buildContact());
    }

    private Contact buildContact() {
        Contact recipeKeeperContact = new Contact();
        recipeKeeperContact.setName("Guilherme Correia");
        recipeKeeperContact.setEmail("guilherme.tusso@gmail.com");
        return recipeKeeperContact;
    }

    private License buildLicense() {
        License recipeKeeperLicense = new License();
        recipeKeeperLicense.setName("Apache 2.0");
        recipeKeeperLicense.setUrl("http://www.apache.org/licenses/LICENSE-2.0");
        return recipeKeeperLicense;
    }

}
