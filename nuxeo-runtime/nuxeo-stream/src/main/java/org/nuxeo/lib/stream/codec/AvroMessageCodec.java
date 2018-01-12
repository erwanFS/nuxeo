/*
 * (C) Copyright 2018 Nuxeo SA (http://nuxeo.com/) and others.
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
 *
 * Contributors:
 *     bdelbosc
 */
package org.nuxeo.lib.stream.codec;

import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.SchemaStore;
import org.apache.avro.reflect.ReflectData;

/**
 * Avro Single object encoding: magic 2 bytes + schema fingerprint 8 bytes + avro binary.
 * See https://avro.apache.org/docs/current/spec.html#single_object_encoding
 *
 * When using a SchemaStore the writer and reader schemas can evolve.
 *
 * @since 10.1
 */
public class AvroMessageCodec<T> implements Codec<T> {
    protected final Class<T> messageClass;

    protected Schema schema;

    protected BinaryMessageEncoder<T> encoder;

    protected BinaryMessageDecoder<T> decoder;

    public AvroMessageCodec(Class<T> messageClass, SchemaStore store) {
        this.messageClass = messageClass;
        // TODO: check if class impl an interface that returns an explicit schema
        schema = ReflectData.get().getSchema(messageClass);
        encoder = new BinaryMessageEncoder<>(ReflectData.get(), schema);
        decoder = new BinaryMessageDecoder<>(ReflectData.get(), schema, store);
    }

    public AvroMessageCodec(Class<T> messageClass) {
        this(messageClass, null);
    }

    @Override
    public byte[] encode(T message) {
        try {
            return encoder.encode(message).array();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public T decode(byte[] data) {
        try {
            return decoder.decode(data, null);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
