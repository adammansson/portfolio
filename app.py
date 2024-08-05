from flask import Flask, render_template, request, make_response
import requests
from datetime import timedelta
import uuid

app = Flask(__name__)


def get_response(method, domain):
    method = method.strip().upper()
    url = "https://" + domain

    return requests.request(method, url)


@app.get("/")
def index():
    return render_template("index.html")


@app.post("/requestform")
def request_form():
    method = request.form["method"]
    domain = request.form["domain"]
    response = get_response(method, domain)
    response_id = str(uuid.uuid1())

    text = response.text
    status = f'{response.status_code} {response.reason}'
    elapsed = f'{response.elapsed / timedelta(milliseconds=1)} ms'
    size = f'{len(response.content)} B'

    response = make_response(render_template(
        "responseviewer.html", text=text, status=status, elapsed=elapsed, size=size
    ))

    response.set_cookie('response_id', response_id)

    return response
