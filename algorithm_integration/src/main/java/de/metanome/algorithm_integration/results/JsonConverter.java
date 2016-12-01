/**
 * Copyright 2015-2016 by Metanome Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.metanome.algorithm_integration.results;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Converts a object from a string to Json and vice versa.
 *
 * @param <T> the type object, which should be converted
 */
public class JsonConverter<T> {

  ObjectMapper mapper =
    new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

  /**
   * Converts the given object to a json string.
   *
   * @param type the object
   * @return the json string
   * @throws com.fasterxml.jackson.core.JsonProcessingException if type could not be converted to a json string
   */
  public String toJsonString(T type) throws JsonProcessingException {
    return this.mapper.writeValueAsString(type);
  }

  public List<String> toJsonStrings(List<T> tList) {
    List<String> result = new ArrayList<>();
    for (T t : tList) {
      try {
        result.add(this.mapper.writeValueAsString(t));
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  /**
   * Converts the given json string to an object of the given class.
   *
   * @param json  the json string
   * @param clazz the class of the object
   * @return the object
   * @throws java.io.IOException if json string could not be converted into type
   */
  public T fromJsonString(String json, Class<T> clazz) throws IOException {
    return this.mapper.readValue(json, clazz);
  }

  /**
   * Adds mixIn class so that corresponding target class is handled as if it had all the annotations
   * has (for purposes of configuring serialization / deserialization), while avoiding unwanted
   * dependencies in the target class
   *
   * @param target target class
   * @param mixIn  mixin class
   */
  public void addMixIn(Class<?> target, Class<?> mixIn) {
    this.mapper.addMixInAnnotations(target, mixIn);
  }

}
