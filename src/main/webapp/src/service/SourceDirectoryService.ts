const url = 'http://localhost:8080'

export async function loadDir(path: string): Promise<string> {

  const requestBody = {
    path: path,
  };

  return new Promise<string>(async function (resolve, reject) {
    const res = await fetch(`${url}/directory`, {
      method: "post",
      headers: {
        "Content-Type": "application/json",
        "x-access-token": "token-value",
      },
      body: JSON.stringify(requestBody),
    });

    if (!res.ok) {
      const message = `Fetch error: ${res.status} - ${res.statusText}`;
      reject(message);
      return;
    }

    const data = await res.json();

    resolve(data);
  });
}

export async function listDirs(): Promise<string[]> {


  return new Promise<string[]>(async function (resolve, reject) {
    const res = await fetch(`${url}/directories`, {
      method: "get",
      headers: {
        "Content-Type": "application/json",
        "x-access-token": "token-value",
      },
    });

    if (!res.ok) {
      const message = `Fetch error: ${res.status} - ${res.statusText}`;
      reject(message);
      return;
    }

    const data = await res.json();

    resolve(data);
  });
}
