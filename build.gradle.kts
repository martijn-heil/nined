/*
 * MIT License
 *
 * Copyright (c) 2023 Martijn Heil
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import org.apache.tools.ant.filters.ReplaceTokens
import java.net.URI
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel

plugins {
    `java-gradle-plugin`
    idea
}

group = "me.martijn_heil.nined"
version = "1.0-SNAPSHOT"
description = "More blocks"

apply {
    plugin("java")
    plugin("idea")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks {
    withType<ProcessResources> {
        filter(mapOf(Pair("tokens", mapOf(Pair("version", version)))), ReplaceTokens::class.java)
    }
}

defaultTasks = mutableListOf("shadowJar")

repositories {
    maven { url = URI("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }

    mavenCentral()
    mavenLocal()
}

idea {
    project {
        languageLevel = IdeaLanguageLevel("17")
    }
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

dependencies {
    implementation("org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT") { isChanging = true }
}
