from fastapi import FastAPI
from kafka import KafkaProducer
import json, uuid, os

app = FastAPI()

producer = KafkaProducer(
    bootstrap_servers=[os.getenv("KAFKA_BOOTSTRAP", "hl15.zil:9094")],
    value_serializer=lambda v: json.dumps(v).encode("utf-8")
)
TOPIC = os.getenv("KAFKA_TOPIC", "hl02")

@app.post("/students/")
async def create_student(body: dict):
    body["id"] = str(uuid.uuid4())
    msg = {
        "entity": "STUDENT",
        "operation": "POST",
        "payload": json.dumps(body)
    }
    producer.send(TOPIC, value=msg, partition=0)
    producer.flush()
    return {"id": body["id"]}
