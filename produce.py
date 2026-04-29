from kafka import KafkaProducer
import json

producer = KafkaProducer(
    bootstrap_servers=["hl15.zil:9094"],
    value_serializer=lambda v: json.dumps(v).encode("utf-8")
)

TOPIC = "hl02"

messages = [
    {
        "entity": "STUDENT",
        "operation": "POST",
        "payload": json.dumps({
            "id": "44ecd146-b015-4b37-a2d2-7d23538d2119",
            "fio": "Иванов Иван Иванович",
            "studentCard": "СТ-001",
            "enrollmentYear": 2024
        })
    },
    {
        "entity": "COURSE",
        "operation": "POST",
        "payload": json.dumps({
            "id": "5d07050c-0dd1-4fca-bf54-ba15ae3d926f",
            "code": "CS11",
            "name": "ПЯВУ",
            "teacher": "Симонов А.В.",
            "credits": 4
        })
    },
    {
        "entity": "ENROLLMENT",
        "operation": "POST",
        "payload": json.dumps({
            "studentId": "44ecd146-b015-4b37-a2d2-7d23538d2118",
            "courseId": "5d07050c-0dd1-4fca-bf54-ba15ae3d926f"
        })
    },
    {
        "entity": "STUDENT",
        "operation": "DEL",
        "payload": "11111111-1111-1111-1111-111111111111"
    }
]

for msg in messages:
    result = producer.send(TOPIC, value=msg).get(timeout=30)
    print(f"sent to partition={result.partition}, offset={result.offset}, message={msg}")

producer.flush()
producer.close()